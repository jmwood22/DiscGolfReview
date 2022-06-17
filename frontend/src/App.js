import React, { Component } from 'react';
import './App.css';
import Home from './nav/Home';
import { BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import CourseList from './course/CourseList';
import CourseEdit from './course/CourseEdit';


class App extends Component {

  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/courses' exact={true} component={CourseList}/>
            <Route path='/courses/:id' component={CourseEdit}/>
          </Switch>
        </Router>
    )
  }
}

export default App;
