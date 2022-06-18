import React, {Component} from 'react';
import {Container, Nav, Navbar, NavbarBrand, NavItem, NavLink} from 'reactstrap';
import {Link} from 'react-router-dom';

export default class AppNavbar extends Component {
    constructor(props) {
        super(props);
        this.state = {isOpen: false};
        this.toggle = this.toggle.bind(this);
    }

    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    render() {
        return <div>
            <Navbar color="dark" dark expand="md">
                <Container>
                   <NavbarBrand tag={Link} to="/">DiscGolfReview</NavbarBrand>
                   <Nav className="ml-auto" navbar>
                       <NavItem>
                           <NavLink tag={Link} to="/courses">Courses</NavLink>
                       </NavItem>
                       <NavItem>
                           <NavLink tag={Link} to="/login">Login</NavLink>
                       </NavItem>
                       <NavItem>
                           <NavLink tag={Link} to="/register">Sign Up</NavLink>
                       </NavItem>
                   </Nav>
                </Container>
            </Navbar>
        </div>;
    }
}