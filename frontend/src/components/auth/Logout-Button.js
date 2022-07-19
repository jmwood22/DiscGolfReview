import {useAuth0} from "@auth0/auth0-react";
import React from "react";
import configData from "../../config.json";

export const LogoutButton = () => {
    const { user, getAccessTokenSilently, logout } = useAuth0();

    function handleLogout(event) {
        getAccessTokenSilently({
            audience: configData.audience
        }).then(token => {
            if(user) {
                const loginEvent = {
                    "@class": "com.jmwood.sample.discgolfreview.model.event.AuthEvent",
                    user,
                    date: +new Date().getTime(),
                    sessionId: sessionStorage.getItem("session_id"),
                    type: "LOGOUT"
                }

                fetch("/events/auth", {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                        Authorization: 'Bearer ' + token
                    },
                    body: JSON.stringify(loginEvent)
                }).then(() => {
                    sessionStorage.clear();
                    logout({
                        returnTo: window.location.origin
                    });
                })
            }
        })
    }

    return (
        <a className="nav-link" onClick={handleLogout}>Logout</a>
    )
}