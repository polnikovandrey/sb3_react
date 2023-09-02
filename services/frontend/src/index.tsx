import React from 'react';
import {Provider} from 'react-redux';
import store from "./store/store";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './index.scss';
import App from './App';
import {createRoot} from "react-dom/client";
import {Client} from "@stomp/stompjs";

// TODO JWT https://stomp-js.github.io/faqs/faqs.html#p-can-i-use-token-based-authentication-with-these-libraries-p
const client = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    onChangeState: state => console.log(`Stomp client state changed: ${state}`),
    onConnect: frame => {
        console.log(`Stomp client connected: ${frame.headers['message']}. Details: ${frame.body}`)
        client.subscribe('/topic/emailConfirmed', message => {      // TODO user/{recipientId}/topic/emailConfirmed
            console.log(`Received: ${JSON.parse(message.body)}`);
        })
    },
    onDisconnect: frame => console.log(`Stomp client disconnected: ${frame.headers['message']}. Details: ${frame.body}`),
    onStompError: frame => console.log(`Stomp client reported error: ${frame.headers['message']}. Details: ${frame.body}`),
    debug: msg => console.log(msg),
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    reconnectDelay: 5000
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