import {useAuth0} from "@auth0/auth0-react";
import React from "react";

export const LoginButton = () => {
    const { loginWithRedirect } = useAuth0();

    function handleLogin(event) {
        loginWithRedirect()
            .then(() => console.log("Login Successful"))
    }

    return (
        <a className="nav-link" onClick={handleLogin}>Login</a>
    )
}