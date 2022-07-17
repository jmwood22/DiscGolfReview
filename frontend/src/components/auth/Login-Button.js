import {useAuth0} from "@auth0/auth0-react";
import React from "react";

export const LoginButton = () => {
    const { loginWithRedirect } = useAuth0();

    function handleLogin(event) {
        loginWithRedirect({
            appState: {
                returnTo: window.location.pathname
            }
        })
    }

    return (
        <a className="nav-link" onClick={handleLogin}>Login</a>
    )
}