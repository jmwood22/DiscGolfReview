# Disc Golf Review by Jeff Wood

## What is Disc Golf Review?

Disc Golf Review is a simple application designed to track the activity of logged-in user.

## Tech Stack

* Java, Spring Boot
* React
* MongoDB
* Kafka Streams
* Auth0

## Overview

To start, what activities are being tracked? I've tried to keep it relatively light and only track the following four
Event types:

* [`AuthEvent`](https://github.com/jmwood22/DiscGolfReview/blob/master/src/main/java/com/jmwood/sample/discgolfreview/model/event/AuthEvent.java):
  User Login and Logout events
* [`NavEvent`](https://github.com/jmwood22/DiscGolfReview/blob/master/src/main/java/com/jmwood/sample/discgolfreview/model/event/NavEvent.java):
  User is directed to a new page
* [`CourseEvent`](https://github.com/jmwood22/DiscGolfReview/blob/master/src/main/java/com/jmwood/sample/discgolfreview/model/event/CourseEvent.java):
  User has created or altered the state of
  a [`Course`](https://github.com/jmwood22/DiscGolfReview/blob/master/src/main/java/com/jmwood/sample/discgolfreview/model/Course.java)
  record
* [`CLickEvent`](https://github.com/jmwood22/DiscGolfReview/blob/master/src/main/java/com/jmwood/sample/discgolfreview/model/event/ClickEvent.java):
  User has clicked an HTML component that has tracking enabled

### Event Origin

Whenever one of these Events occurs, the front-end will send a `POST` request to the back-end to submit the Event for
processing.
Each of these requests are tagged with a Session ID. The Session ID is a UUID minted after a successful User Login
`AuthEvent` and then saved to Session Storage. Please refer to the
[`/frontend/src/components/tracking`](https://github.com/jmwood22/DiscGolfReview/tree/master/frontend) package to see
how some of these `POST` requests are structured and the manner in which they are sent.

### Event Publication

The back-end has a collection of
[`REST endpoints`](https://github.com/jmwood22/DiscGolfReview/tree/master/src/main/java/com/jmwood/sample/discgolfreview/controller)
listening for new Events to come in. For some Events, they may need some enrichment before being sent off to their final
destination, a Kafka topic. Each of the four Event types listed above have their own Kafka topic. The topics are
partitioned by Session ID, meaning that the Events associated with a particular Session ID will be handled by the same
consumer thread.

### Event Aggregation

The [`Kafka Streams processor`](https://github.com/jmwood22/DiscGolfReview/tree/master/src/main/java/com/jmwood/sample/discgolfreview/kafka/streams)
then will merge the topics into a single stream and aggregate the Events sharing a Session ID into a single
[`SessionActivity`](https://github.com/jmwood22/DiscGolfReview/blob/master/src/main/java/com/jmwood/sample/discgolfreview/model/SessionActivity.java)
record. These new records are published to their own Kafka topic but are also update in the database
each time a new Event is appended to them.

## How to Run

The root folder of this repository contains a
functional [`docker-compose.yml`](https://github.com/jmwood22/DiscGolfReview/blob/master/docker-compose.yml)
file. The front-end and back-end have Dockerfiles defined should you want to make any changes to the images. The compose
file has build definitions for the images should you do so, just uncomment them from the compose file and run:

```console
$ docker compose build
```

To run the application, you can use the compose file as shown below:

```console
$ docker compose up -d
```

### Interaction

* You should now be able to access the application via [`localhost:3000`](http://localhost:3000).
* You can populate the application with some sample course data
  via [`localhost:8080/courses/addSampleCourses`](http://localhost:8080/courses/addSampleCourses)
* If you have something like [`MongoDB Compass`](https://www.mongodb.com/products/compass) installed you can view the
  database at `localhost:4000`.
* Sign Up for an account via the Login button at the top which will redirect to an Auth0 login/signup page.
  * The email doesn't need to be verified, but it does have to be unique. Something like fake@email.com will work as
    long as you use it first!
  * Auth0 will ask for your permission to allow DiscGolfReview to access your account when signing up.
* Click around and add/alter some Course data.
* View the `SessionActivity` records being updated in its collection in the database or view the logs in real-time with:

```console
$ docker logs backend --follow
```

> Note: The Kafka Stream processing component will not start aggregating Events until each topic has been created which
> will only happen once each type of Event has occurred. But don't worry, all Events will eventually be processed unless
> the volume is destroyed first. 