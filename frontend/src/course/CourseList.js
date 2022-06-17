import React, { Component } from "react";
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from '../nav/AppNavbar';
import { Link } from 'react-router-dom';

class CourseList extends Component {

    constructor(props) {
        super(props);
        this.state = {courses: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/courses')
            .then(response => response.json())
            .then(data => this.setState({courses: data}));
    }

    async remove(id) {
        await fetch('courses/${id}', {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedCourses = [...this.state.courses].filter(i => i.id !== id);
            this.setState({courses: updatedCourses});
        });
    }

    render() {
        const {courses} = this.state;

        const courseList = courses.map(course => {
            return <tr key={course.id}>
                <td style={{whiteSpace: 'nowrap'}}>{course.name}</td>
                <td>{course.location}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/courses/" + course.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(course.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/courses/new">Add Course</Button>
                    </div>
                    <h3>Courses</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">Location</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {courseList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        )
    }
}
export default CourseList;