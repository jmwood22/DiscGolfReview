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
        this.state = {
            course: this.emptyItem
        }
    }

    async componentDidMount() {
        const course = await (await fetch('/courses/' + this.props.match.params.id)).json();
        this.setState({course: course});
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
                                <Button tag={Link} to={"/courses/edit/" + course.id}>Edit</Button>
                            </div>
                        </div>
                        <div className="row">
                            <UncontrolledAccordion open="1">
                                <AccordionItem>
                                    <AccordionHeader targetId="1">Location</AccordionHeader>
                                    <AccordionBody  accordionId="1">{course.location}</AccordionBody>
                                </AccordionItem>
                                <AccordionItem>
                                    <AccordionHeader targetId="2">Amenities</AccordionHeader>
                                    <AccordionBody accordionId="2">{course.amenities}</AccordionBody>
                                </AccordionItem>
                            </UncontrolledAccordion>
                        </div>
                    </div>

                    <div className="col-md-9">
                        <Card>
                            <CardImg src={course.defaultImageUrl}/>
                            <CardBody  className="bg-light">
                                <CardTitle>{course.name}</CardTitle>
                                <CardText>{course.description}</CardText>
                            </CardBody>
                        </Card>

                        <div className="mb-5 mt-3 p-3 bg-light border border-light rounded">
                            <Button className="float-end" tag={Link} to={"/courses/edit/review/" + course.id}>Leave a Review</Button>
                            <h4 className="p-1">Reviews</h4>
                            <hr/>
                            <div>
                                {
                                    course.reviews ? course.reviews.map(review => {
                                        return (
                                            <div className="row">
                                                <div className="col-md-12">
                                                    <strong>{review.author.nickname}</strong>{' '}
                                                    <span>
                                                        {review.rating}/5
                                                    </span>
                                                    <p>{review.text}</p>
                                                </div>
                                            </div>
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