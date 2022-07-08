import React from "react";
import {useAuth0} from "@auth0/auth0-react";

export const ClickTrackingComponent = ({name, eventType, component}) => {

    const user = useAuth0();

    function trackClick(event) {
        console.log("Click on %s by User: %s. Click: ", name, JSON.stringify(user), event);
    }

    return (
        <span onClick={trackClick}>
            {component}
        </span>
    );
}