import {useLocation} from "react-router-dom";
import {useEffect} from "react";

export const useNavigationTracking = (user) => {
    let location = useLocation();

    useEffect(() => {
        console.log("User: %s. Page view: %s", JSON.stringify(user), location.pathname);
    }, [location])
}