import React from "react";
import {useHistory} from "react-router-dom";
import {v4} from "uuid";
import {Auth0Provider} from "@auth0/auth0-react";

export const Auth0ProviderWithRedirectCallback = ({children, ...props}) => {
    const history = useHistory();

    const onRedirectCallback = (appState) => {
        const sessionUuid = v4();
        sessionStorage.setItem("session_id", sessionUuid);
        history.push({
            pathname: '/loginredirect',
            state: {
                appState
            }
        });
    }

    return (
        <Auth0Provider onRedirectCallback={onRedirectCallback} {...props}>
            {children}
        </Auth0Provider>
    )
}