import {useLocation} from "react-router-dom";
import {useEffect} from "react";
import {useAuth0} from "@auth0/auth0-react";
import configData from "../../config.json"


export const useNavigationTracking = () => {
    const location = useLocation();
    const {user, getAccessTokenSilently} = useAuth0();

    useEffect(() => {

        getAccessTokenSilently({
            audience: configData.audience
        })
            .then(token => {
                const navEvent = {
                    "@class": "com.jmwood.sample.discgolfreview.model.event.NavEvent",
                    user,
                    date: +new Date().getTime(),
                    path: location.pathname,
                    rawLocationJson: JSON.stringify(location),
                    sessionId: sessionStorage.getItem("session_id")
                }
                if (user) {
                    fetch("/events/nav", {
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json',
                            Authorization: 'Bearer ' + token
                        },
                        body: JSON.stringify(navEvent)
                    })
                }
            })
    }, [location, user])
}