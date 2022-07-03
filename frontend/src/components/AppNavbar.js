import React from 'react';
import {Container, Nav, Navbar, NavbarBrand, NavItem, NavLink} from 'reactstrap';
import {Link} from 'react-router-dom';
import {AuthenticationButton} from "./auth/Authentication-Button";
import {ClickTrackingComponent} from "./ClickTrackingComponent";

export const AppNavbar = () => {

    return <div id="app-navbar">
        <Navbar color="dark" dark expand="md">
            <Container>
                <ClickTrackingComponent name="Navbar Brand" component={
                    <NavbarBrand className="float-start" tag={Link} to="/">DiscGolfReview</NavbarBrand>
                }/>
                <Nav className="ml-auto float-end" navbar>
                    <ClickTrackingComponent name="AppNavBar Courses" component={
                        <NavItem>
                            <NavLink tag={Link} to="/courses">Courses</NavLink>
                        </NavItem>
                    }/>
                   <NavItem>
                       <AuthenticationButton/>
                   </NavItem>
               </Nav>
            </Container>
        </Navbar>
    </div>;
}