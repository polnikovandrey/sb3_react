import React from 'react';
import {Provider} from 'react-redux';
import store from "./store/store";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './index.scss';
import App from './App';
import {createRoot} from "react-dom/client";

createRoot(document.getElementById('root') as HTMLElement).render(
    <React.StrictMode>
        <Provider store={store}>
            <App/>
        </Provider>,
    </React.StrictMode>
);