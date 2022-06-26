import React from 'react';
import '../App.css';
import {AppNavbar} from '../components/AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container, UncontrolledCarousel } from 'reactstrap';
import {useAuth0} from "@auth0/auth0-react";

const exampleCourses = [
    {
        src: 'https://i.imgur.com/WgbzT3Z.jpeg'
    }
];

export const Home = () => {
    const { user } = useAuth0();

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
};