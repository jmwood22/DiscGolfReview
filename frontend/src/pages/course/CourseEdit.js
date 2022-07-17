import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import {AppNavbar} from '../../components/AppNavbar';
import {withAuth0} from "@auth0/auth0-react";
import configData from "../../config.json"
import {ClickTrackingComponent} from "../../components/tracking/ClickTrackingComponent";

class CourseEdit extends Component {

    emptyCourse = {
        name: '',
        location: '',
        description: '',
        defaultImageUrl: '',
        amenities: ''
    };

    constructor(props) {
        super(props);
        const course = props.location.state?.course;
        console.log("Props", props);
        console.log("course: ", course);
        this.state = {
            course: course ? course : this.emptyCourse,
            redirectPath: (course) ? '/courses/' + course.id : '/courses'
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        const {course} = this.state;
        if(this.props.match.params.id !== 'new' && course===this.emptyCourse) {
            fetch('/courses/' + this.props.match.params.id)
                .then(response => response.json())
                .then(data => this.setState({course: data}))
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        let course = {...this.state.course};
        course[name] = value;
        this.setState({course});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {course, redirectPath} = this.state;
        const { getAccessTokenSilently, user } = this.props.auth0;

        getAccessTokenSilently({
            audience: configData.audience
        }).then(token => {
            if(!course.id) {
                course.author = user;
            }

            fetch('/courses' + (course.id ? '/' + course.id : ''), {
                method: (course.id) ? 'PUT' : 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'User': JSON.stringify(user),
                    'Session-Id': sessionStorage.getItem("session_id"),
                    Authorization: 'Bearer ' + token
                },
                body: JSON.stringify(course),
            })
                .then(response => response.json())
                .then(data => this.props.history.push({
                    pathname: redirectPath,
                    state: {
                        course: data
                    }
                }))
        });
    }

    remove(id) {
        const { getAccessTokenSilently, user } = this.props.auth0;

        getAccessTokenSilently({
            audience: configData.audience
        }).then(token => {
            fetch('/courses/' + id, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'User': JSON.stringify(user),
                    'Session-Id': sessionStorage.getItem("session_id"),
                    Authorization: 'Bearer ' + token
                }
            }).then(() => this.props.history.push('/courses'))
        });
    }

    render() {
        const {course, redirectPath} = this.state;
        const { user } = this.props.auth0;
        const isNewForm = this.props.match.params.id !== 'new';
        const title = <h2>{ isNewForm ? 'Edit Course' : 'Add  Course'}</h2>

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={course.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="location">Location</Label>
                        <Input type="text" name="location" id="location" value={course.location || ''}
                               onChange={this.handleChange} autoComplete="location"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="name">Description</Label>
                        <Input type="text" name="description" id="description" value={course.description || ''}
                               onChange={this.handleChange} autoComplete="description"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="location">Amenities</Label>
                        <Input type="text" name="amenities" id="amenities" value={course.amenities || ''}
                               onChange={this.handleChange} autoComplete="amenities"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="location">Image Url</Label>
                        <Input type="text" name="defaultImageUrl" id="defaultImageUrl" value={course.defaultImageUrl || ''}
                               onChange={this.handleChange} autoComplete="defaultImageUrl"/>
                    </FormGroup>
                    <FormGroup>
                        <ClickTrackingComponent name={"Edit Course Submit Button for Course with ID " + course.id} component={
                            <Button color="primary" type="submit">Save</Button>
                        }/>
                        {' '}
                        { isNewForm && user.sub === course.author?.sub &&
                        <ClickTrackingComponent name={"Edit Course Delete Button for Course with ID " + course.id} component={
                            <Button color="danger" onClick={() => this.remove(course.id)}>Delete</Button>
                        }/>
                        }{' '}
                        <ClickTrackingComponent name={"Edit Course Cancel Button for Course with ID " + course.id} component={
                            <Button color="secondary" tag={Link} to={redirectPath}>Cancel</Button>
                        }/>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withAuth0(CourseEdit);