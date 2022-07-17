import React, {Component} from "react";
import {Button, Card, CardBody, CardImg, CardText, CardTitle, Col, Container, Row} from 'reactstrap';
import {AppNavbar} from '../../components/AppNavbar';
import {Link} from 'react-router-dom';
import {ClickTrackingComponent} from "../../components/ClickTrackingComponent";

class CourseList extends Component {

    constructor(props) {
        super(props);
        this.state = {courses: []};
    }

    componentDidMount() {
        fetch('/courses')
            .then(response => response.json())
            .then(data => this.setState({courses: data}));
    }

    render() {
        const {courses} = this.state;

        const courseCards = courses.map(course => {
            return (
                <Col key={course.id} className="d-flex">
                    <Card>
                        <CardImg
                            src={course.defaultImageUrl}
                        />
                        <CardBody  className="bg-light">
                            <CardTitle>{course.name}</CardTitle>
                            <CardText>{course.description}</CardText>
                            <ClickTrackingComponent name={"View More Info Button For Course with ID " + course.id} component={
                                <Button name="View More Info Button" size="sm" color="primary"
                                        tag={Link}
                                        to={{
                                            pathname: "/courses/" + course.id,
                                            state: {
                                                course: course
                                            }
                                        }}>View More Info</Button>
                            }/>
                        </CardBody>
                    </Card>
                </Col>
            )
        });

        return (
            <div>
                <AppNavbar/>
                <Container className="mt-5">
                    <div className="float-end">
                        <ClickTrackingComponent name="Add New Course Button" component={
                            <Button color="success" tag={Link} to="/courses/edit/new">Add Course</Button>
                        }/>
                    </div>
                    <h3>Courses</h3>
                    <Container className="mt-3">
                        <Row xs={3} className="d-flex">
                            {courseCards}
                        </Row>
                    </Container>
                </Container>
            </div>
        )
    }
}
export default CourseList;