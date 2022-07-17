import React, {Component} from "react";
import {AppNavbar} from "../../components/AppNavbar";
import {Link, withRouter} from "react-router-dom";
import {
    AccordionBody,
    AccordionHeader,
    AccordionItem,
    Button,
    Card,
    CardBody,
    CardImg,
    CardText,
    CardTitle,
    Container,
    UncontrolledAccordion
} from "reactstrap";
import {Review} from "../../components/Review";
import {ClickTrackingComponent} from "../../components/tracking/ClickTrackingComponent";

class CourseView extends Component {

    emptyItem = {
        name: '',
        location: '',
        description: '',
        defaultImageUrl: '',
        amenities: '',
        author: {
            nickname: ''
        },
        reviews: [
            {
                author: {
                    nickname: ''
                },
                text: '',
                rating: 0
            }
        ]
    };

    constructor(props) {
        super(props);
        const course = props.location.state?.course;
        this.state = {
            course: course ? course : this.emptyItem,
        }
    }

    componentDidMount() {
        const {course} = this.state;
        if(course === this.emptyItem) {
            fetch('/courses/' + this.props.match.params.id)
                .then(response => response.json())
                .then(data => this.setState({course: data}))
        }
    }

    render() {
        const {course} = this.state;

        return <div>
            <AppNavbar/>

            <Container fluid="lg" className="mt-5">
                <div className="row">
                    <div className="col-md-3">
                        <div className="row">
                            <div className="col-md-9">
                                <p>{course.name}</p>
                            </div>
                            <div className="col-md-3">
                                <ClickTrackingComponent name={"Edit Button for Course with ID " + course.id} component={
                                    <Button tag={Link}
                                            to={{
                                                pathname: "/courses/edit/" + course.id,
                                                state: {
                                                    course: course
                                                }
                                            }}>Edit</Button>
                                }/>
                            </div>
                        </div>
                        <div className="row">
                            <UncontrolledAccordion open={1}>
                                <ClickTrackingComponent name={"Location AccordionItem for Course with ID " + course.id} component={
                                    <AccordionItem>
                                        <AccordionHeader targetId={1}>Location</AccordionHeader>
                                        <AccordionBody accordionId={1}>{course.location}</AccordionBody>
                                    </AccordionItem>
                                }/>
                                <ClickTrackingComponent name={"Amenities AccordionItem for Course with ID " + course.id} component={
                                    <AccordionItem>
                                        <AccordionHeader targetId={2}>Amenities</AccordionHeader>
                                        <AccordionBody accordionId={2}>{course.amenities}</AccordionBody>
                                    </AccordionItem>
                                }/>
                            </UncontrolledAccordion>
                        </div>
                    </div>

                    <div className="col-md-9">
                        <Card>
                            <CardImg src={course.defaultImageUrl}/>
                            <CardBody className="bg-light">
                                <CardTitle>{course.name}</CardTitle>
                                <CardText>{course.description}</CardText>
                            </CardBody>
                        </Card>

                        <div className="mb-5 mt-3 p-3 bg-light border border-light rounded">
                            <ClickTrackingComponent name={"Leave a Review Button for Course with ID " + course.id} component={
                                <Button className="float-end"
                                        tag={Link}
                                        to={{
                                            pathname: "/courses/edit/review/" + course.id,
                                            state: {
                                                course: course
                                            }
                                }}>Leave a Review</Button>
                            }/>
                            <h4 className="p-1">Reviews</h4>
                            <hr/>
                            <div>
                                {
                                    course.reviews ? course.reviews.map(review => {
                                        return (
                                            <Review review={review}/>
                                        )
                                    }) :
                                        <span>Be the first to write a review!</span>
                                }
                            </div>
                        </div>
                    </div>
                </div>
            </Container>
        </div>
    }
}

export default withRouter(CourseView);