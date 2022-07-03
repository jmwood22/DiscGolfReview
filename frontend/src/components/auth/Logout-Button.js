import {useAuth0} from "@auth0/auth0-react";
import React from "react";

export const LogoutButton = () => {
    const { logout } = useAuth0();

    function handleLogout(event) {
        console.log("Logging out user");
        logout({
            returnTo: window.location.origin
        });
    }

    return (
        <a className="nav-link" onClick={handleLogout}>Logout</a>
    )
}