import configData from "../../config.json";
import {useAuth0} from "@auth0/auth0-react";
import {useHistory} from "react-router-dom";

export const TrackLoginEvent = (props) => {

    const { user, getAccessTokenSilently } = useAuth0();
    const history = useHistory();

    getAccessTokenSilently({
        audience: configData.audience
    }).then(token => {
        if(user) {
            const loginEvent = {
                "@class": "com.jmwood.sample.discgolfreview.model.event.AuthEvent",
                user,
                date: +new Date().getTime(),
                sessionId: sessionStorage.getItem("session_id"),
                type: "LOGIN"
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
                history.push(props.location.state?.appState?.returnTo || '/');
            })
        }
    })
}