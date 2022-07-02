import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {App} from './App';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Auth0Provider} from "@auth0/auth0-react";

import configData from "./config.json";
import {BrowserRouter} from "react-router-dom";

const providerConfig = {
    domain: configData.domain,
    clientId: configData.clientId,
    audience: configData.audience,
    redirectUri: window.location.origin,
    useRefreshTokens: true,
    cacheLocation: "localstorage"
};

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
      <Auth0Provider {...providerConfig}>
          <BrowserRouter>
              <App />
          </BrowserRouter>
      </Auth0Provider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
