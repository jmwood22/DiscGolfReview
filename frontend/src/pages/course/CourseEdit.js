import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import {AppNavbar} from '../../components/AppNavbar';

class CourseEdit extends Component {

    emptyItem = {
        name: '',
        location: '',
        description: '',
        defaultImageUrl: '',
        amenities: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.remove = this.remove.bind(this);
    }

    async componentDidMount() {
        if(this.props.match.params.id !== 'new') {
            const course = await (await fetch('/courses/' + this.props.match.params.id)).json();
            this.setState({item: course});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        await fetch('/courses' + (item.id ? '/' + item.id : ''), {
            method: (item.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });

        this.props.history.push('/courses');
    }

    async remove(id) {
        await fetch('/courses/' + id, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });
    }

    render() {
        const {item} = this.state;
        const title = <h2>{item.id ? 'Edit Course' : 'Add  Course'}</h2>

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={item.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="location">Location</Label>
                        <Input type="text" name="location" id="location" value={item.location || ''}
                               onChange={this.handleChange} autoComplete="location"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="name">Description</Label>
                        <Input type="text" name="description" id="description" value={item.description || ''}
                               onChange={this.handleChange} autoComplete="description"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="location">Amenities</Label>
                        <Input type="text" name="amenities" id="amenities" value={item.amenities || ''}
                               onChange={this.handleChange} autoComplete="amenities"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="location">Image Url</Label>
                        <Input type="text" name="defaultImageUrl" id="defaultImageUrl" value={item.defaultImageUrl || ''}
                               onChange={this.handleChange} autoComplete="defaultImageUrl"/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="danger" onClick={() => this.remove(item.id)} tag={Link} to="/courses">Delete</Button>{' '}
                        <Button color="secondary" tag={Link} to="/courses">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withRouter(CourseEdit);