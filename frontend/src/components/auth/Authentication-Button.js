import { useAuth0 } from "@auth0/auth0-react";
import React from "react";
import {LogoutButton} from "./Logout-Button";
import {LoginButton} from "./Login-Button";

export const AuthenticationButton = () => {
    const { isAuthenticated } = useAuth0();

    return isAuthenticated ? <LogoutButton/> : <LoginButton/>;
}