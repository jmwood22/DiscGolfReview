import React from 'react';
import '../App.css';
import {AppNavbar} from '../components/AppNavbar';
import {Link} from 'react-router-dom';
import {Button, Container, UncontrolledCarousel} from 'reactstrap';
import {ClickTrackingComponent} from "../components/tracking/ClickTrackingComponent";

const exampleCourses = [
    {
        src: 'https://i.imgur.com/WgbzT3Z.jpeg'
    }
];

export const Home = () => {

    return (
        <div>
            <AppNavbar/>
            <Container id="home-banner">
                <h1 className="display-3 mb-3">Welcome to DiscGolfReview!</h1>
                <ClickTrackingComponent name="Home View Courses Button" component={
                    <Button tag={Link} to="/courses">View Courses</Button>
                }/>

            </Container>
            <div id="home-slideshow">
                <UncontrolledCarousel controls={false} indicators={false} items={exampleCourses} />
            </div>
        </div>
    );
};