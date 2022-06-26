import React from 'react';
import {Container, Nav, Navbar, NavbarBrand, NavItem, NavLink} from 'reactstrap';
import {Link} from 'react-router-dom';
import {AuthenticationButton} from "./auth/Authentication-Button";

export const AppNavbar = () => {

    return <div id="app-navbar">
        <Navbar color="dark" dark expand="md">
            <Container>
               <NavbarBrand tag={Link} to="/">DiscGolfReview</NavbarBrand>
               <Nav className="ml-auto float-end" navbar>
                   <NavItem>
                       <NavLink tag={Link} to="/courses">Courses</NavLink>
                   </NavItem>
                   <NavItem>
                       <AuthenticationButton/>
                   </NavItem>
               </Nav>
            </Container>
        </Navbar>
    </div>;
}