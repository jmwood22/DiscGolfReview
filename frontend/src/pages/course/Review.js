import React, {Component} from "react";
import {withAuth0} from "@auth0/auth0-react";
import configData from "../../config.json"
import {AppNavbar} from "../../components/AppNavbar";
import {Button, Container, Form, FormGroup, Input, Label} from "reactstrap";

class Review extends Component {

    emptyReview = {
        author: {

        },
        text: '',
        rating: 0
    }

    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            review: this.emptyReview
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        let review = {...this.state.review};
        review[name] = value;
        this.setState({review});
    }

    async handleSubmit(event) {
        event.preventDefault();

        const { id, review } = this.state;
        const { getAccessTokenSilently, user } = this.props.auth0;

        getAccessTokenSilently({
            audience: configData.audience
        }).then(token => {
            review.author = user;
            console.log("Review: ", JSON.stringify(review));

            fetch('/courses/reviews/' + id, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'User': JSON.stringify(user),
                    Authorization: 'Bearer ' + token
                },
                body: JSON.stringify(review)
            })
        });

        this.props.history.push('/courses/' + id);
    }

    render() {
        return <div>
            <AppNavbar/>
            <Container>
                <h2>Add a Review</h2>
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="rating">Rating</Label>
                        <Input type="select" name="rating" id="rating" onChange={this.handleChange} autoComplete="rating">
                            <option>0</option>
                            <option>1</option>
                            <option>2</option>
                            <option>3</option>
                            <option>4</option>
                            <option>5</option>
                        </Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for="text">Comments</Label>
                        <Input type="text" name="text" id="text" onChange={this.handleChange} autoComplete="text"/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Submit</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withAuth0(Review);