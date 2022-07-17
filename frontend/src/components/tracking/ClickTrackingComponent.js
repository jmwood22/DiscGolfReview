import React from "react";
import {useAuth0} from "@auth0/auth0-react";
import configData from "../../config.json";

export const ClickTrackingComponent = ({name, component}) => {

    const {user, getAccessTokenSilently} = useAuth0();

    function trackClick(event) {
        getAccessTokenSilently({
            audience: configData.audience
        })
            .then(token => {
                const clickEvent = {
                    user,
                    date: +new Date().getTime(),
                    elementName: name,
                    // rawEventJson: JSON.stringify(event.target),
                    sessionId: sessionStorage.getItem("session_id")
                }
                if (user) {
                    fetch("/events/click", {
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json',
                            Authorization: 'Bearer ' + token
                        },
                        body: JSON.stringify(clickEvent)
                    })
                }
            })
    }

    return (
        <span onClick={trackClick}>
            {component}
        </span>
    );
}