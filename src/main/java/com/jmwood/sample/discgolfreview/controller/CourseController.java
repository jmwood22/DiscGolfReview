package com.jmwood.sample.discgolfreview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmwood.sample.discgolfreview.model.Course;
import com.jmwood.sample.discgolfreview.model.Review;
import com.jmwood.sample.discgolfreview.model.User;
import com.jmwood.sample.discgolfreview.model.event.CourseEvent;
import com.jmwood.sample.discgolfreview.model.event.enums.CourseEventType;
import com.jmwood.sample.discgolfreview.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

  private static final String SESSION_ID_HEADER = "Session-Id";
  private static final String USER_HEADER = "User";

  private final CourseRepository courseRepository;
  private final EventController eventController;

  @GetMapping
  public List<Course> getCourses() {
    log.info("Received request to get all courses");
    List<Course> results = courseRepository.findAll();
    log.info("Retrieved {} courses", results.size());
    return results;
  }

  @GetMapping("/{id}")
  public Course getCourse(@PathVariable String id) {
    log.info("Received request to get Course with id {}", id);
    return courseRepository.findById(id).orElseThrow(RuntimeException::new);
  }

  @PostMapping
  public ResponseEntity createCourse(
      @RequestBody Course course,
      @RequestHeader(SESSION_ID_HEADER) String sessionId,
      @RequestHeader(USER_HEADER) String userJson)
      throws URISyntaxException {
    log.info("Received request to create Course: {}", course);
    CourseEvent event =
        createEvent(
            course, sessionId, extractUserFromJson(userJson), CourseEventType.ADD_NEW_COURSE);
    Course savedCourse = courseRepository.save(course);
    eventController.submitCourseEvent(event);
    return ResponseEntity.created(new URI("/courses/" + savedCourse.getId())).body(savedCourse);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateCourse(
      @PathVariable String id,
      @RequestBody Course course,
      @RequestHeader(SESSION_ID_HEADER) String sessionId,
      @RequestHeader(USER_HEADER) String userJson) {
    log.info("Received request to update Course with id {} to Course: {}", id, course);
    Course currentCourse = courseRepository.findById(id).orElseThrow(RuntimeException::new);
    currentCourse.setName(course.getName());
    currentCourse.setLocation(course.getLocation());
    currentCourse.setDescription(course.getDescription());
    currentCourse.setDefaultImageUrl(course.getDefaultImageUrl());
    currentCourse.setAmenities(course.getAmenities());
    currentCourse = courseRepository.save(currentCourse);

    CourseEvent event =
        createEvent(
            currentCourse,
            sessionId,
            extractUserFromJson(userJson),
            CourseEventType.EDIT_COURSE_DETAIL);
    eventController.submitCourseEvent(event);
    return ResponseEntity.ok(currentCourse);
  }

  @PostMapping("/reviews/{id}")
  public ResponseEntity addReview(
      @PathVariable String id,
      @RequestBody Review review,
      @RequestHeader(SESSION_ID_HEADER) String sessionId,
      @RequestHeader(USER_HEADER) String userJson) {
    log.info("Received request to add the following review to Course with id {}: {}", id, review);
    Course course = courseRepository.findById(id).orElseThrow(RuntimeException::new);
    course.addReview(review);
    course = courseRepository.save(course);

    CourseEvent event =
        createEvent(course, sessionId, extractUserFromJson(userJson), CourseEventType.ADD_REVIEW);
    eventController.submitCourseEvent(event);

    return ResponseEntity.ok(course);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteCourse(
      @PathVariable String id,
      @RequestHeader(SESSION_ID_HEADER) String sessionId,
      @RequestHeader(USER_HEADER) String userJson) {
    log.info("Received request to delete Course with id {}", id);
    CourseEvent event =
        createEvent(
            courseRepository.findById(id).orElseThrow(RuntimeException::new),
            sessionId,
            extractUserFromJson(userJson),
            CourseEventType.DELETE_COURSE);
    eventController.submitCourseEvent(event);
    courseRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/addSampleCourses")
  public ResponseEntity addSampleCourses() {
    log.info("Received request to add sample Courses.");
    Course course1 =
        Course.builder()
            .name("Sample Course 1")
            .location("Nowheresville")
            .description("Just grass and baskets.")
            .defaultImageUrl("https://i.imgur.com/lI9MXsk.jpeg")
            .amenities("Clean bathrooms.")
            .author(getSampleUserByIndex(0))
            .reviews(getSampleReviews())
            .build();
    Course course2 =
        Course.builder()
            .name("Sample Course 2")
            .location("Middle of Nowhere")
            .description("Tight, wooded course.")
            .defaultImageUrl("https://i.imgur.com/cvBuouY.jpeg")
            .amenities("Hiking trails.")
            .author(getSampleUserByIndex(1))
            .reviews(getSampleReviews())
            .build();
    Course course3 =
        Course.builder()
            .name("Maple Hill")
            .location("Leicester, MA")
            .description("Great views, great disc golf.")
            .defaultImageUrl("https://i.imgur.com/WgbzT3Z.jpeg")
            .amenities("Pro shop nearby.")
            .author(getSampleUserByIndex(2))
            .reviews(getSampleReviews())
            .build();

    courseRepository.saveAll(List.of(course1, course2, course3));
    return ResponseEntity.ok().build();
  }

  private User extractUserFromJson(String userJson) {
    ObjectMapper mapper =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      return mapper.readValue(userJson, User.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private CourseEvent createEvent(
      Course course, String sessionId, User user, CourseEventType type) {
    return CourseEvent.builder()
        .sessionId(sessionId)
        .user(user)
        .date(new Date())
        .course(course)
        .type(type)
        .build();
  }

  private List<Review> getSampleReviews() {

    Review review1 = new Review();
    review1.setAuthor(getSampleUserByIndex(0));
    review1.setRating(4);
    review1.setText("Great course, very beginner friendly.");

    Review review2 = new Review();
    review2.setAuthor(getSampleUserByIndex(1));
    review2.setRating(2);
    review2.setText("One of the worst courses I've ever played.");

    Review review3 = new Review();
    review3.setAuthor(getSampleUserByIndex(2));
    review3.setRating(3);
    review3.setText("Bring some bug spray!");

    return List.of(review1, review2, review3);
  }

  private User getSampleUserByIndex(int index) {
    User user1 =
        User.builder()
            .id("sample-user-1")
            .name("John Doe")
            .nickname("John")
            .email("john.doe@fake.com")
            .build();
    User user2 =
        User.builder()
            .id("sample-user-2")
            .name("Jane Doe")
            .nickname("Jane")
            .email("jane.doe@fake.com")
            .build();
    User user3 =
        User.builder()
            .id("sample-user-3")
            .name("Elvis Presley")
            .nickname("Elvis")
            .email("the.king@fake.com")
            .build();

    User[] users = new User[] {user1, user2, user3};

    return users[index % users.length];
  }
}
