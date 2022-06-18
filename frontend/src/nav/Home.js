import React, { Component } from 'react';
import '../App.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container, UncontrolledCarousel } from 'reactstrap';

const exampleCourses = [
    {
        src: 'https://i.imgur.com/WgbzT3Z.jpeg'
    }
];

class Home extends Component {

    render() {
        return (
            <div>
                <AppNavbar/>
                <Container id="home-banner">
                    <h1 className="display-3 mb-3">Welcome to DiscGolfReview!</h1>
                    <Button tag={Link} to="/courses">View Courses</Button>
                </Container>
                <div id="home-slideshow">
                    <UncontrolledCarousel controls={false} indicators={false} items={exampleCourses} />
                </div>
            </div>
        );
    }
}
export default Home;