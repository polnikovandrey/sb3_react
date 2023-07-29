import React from 'react';
import {Provider} from 'react-redux';
import store from "./store/store";
import './bootstrap.min.css';
import './index.scss';
import App from './App';
import {createRoot} from "react-dom/client";

const root = createRoot(document.getElementById("root") as HTMLElement);
root.render(
    <React.StrictMode>
        <Provider store={store}>
            <App/>
        </Provider>,
    </React.StrictMode>
);