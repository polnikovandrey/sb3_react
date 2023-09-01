import React from 'react';
import {Provider} from 'react-redux';
import store from "./store/store";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './index.scss';
import App from './App';
import {createRoot} from "react-dom/client";
import {Client} from "@stomp/stompjs";

const client = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    onConnect: () => {
        console.log('Connected');
        // TODO user/{recipientId}/topic/emailConfirmed
        client.subscribe('/topic/emailConfirmed', message => console.log(`Received: ${message.body}`))
    }
});
client.activate();

const root = createRoot(document.getElementById('root') as HTMLElement);
root.render(
    <React.StrictMode>
        <Provider store={store}>
            <App/>
        </Provider>,
    </React.StrictMode>
);