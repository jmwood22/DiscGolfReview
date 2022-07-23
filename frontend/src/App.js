import React from 'react';
import './App.css';
import {Home} from './pages/Home';
import {Route, Switch} from 'react-router-dom';
import CourseList from './pages/course/CourseList';
import CourseView from './pages/course/CourseView';
import CourseEdit from './pages/course/CourseEdit';
import {ProtectedRoute} from "./components/auth/ProtectedRoute";
import Review from "./pages/course/AddReview";
import {useNavigationTracking} from "./components/tracking/useNavigationTracking";
import {TrackLoginEvent} from "./components/tracking/TrackLoginEvent";

export const App = () => {

    useNavigationTracking();

    return (
        <div>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/courses' exact={true} component={CourseList}/>
            <ProtectedRoute path='/courses/edit/review/:id' component={Review}/>
            <ProtectedRoute path='/courses/edit/:id' component={CourseEdit}/>
            <Route path='/courses/:id' component={CourseView}/>
            <ProtectedRoute path='/loginredirect' component={TrackLoginEvent}/>
          </Switch>
        </div>
    )
};