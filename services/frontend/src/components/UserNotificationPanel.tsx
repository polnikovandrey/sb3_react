import * as React from "react";
import {useEffect} from "react";
import {Alert, Container, Fade} from "react-bootstrap";
import {UserState} from "../store/types";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {addNotification, removeNotification, selectNotificationsState} from "../slice/notificationsSlice";
import {Client} from "@stomp/stompjs";
import {CONFIRM_EMAIL_NOTIFICATION, CONFIRM_EMAIL_SUCCESS_NOTIFICATION} from "../notifications";
import {selectUserInfo} from "../slice/userSlice";

const client = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    onChangeState: state => console.log(`Stomp client state changed: ${state}`),
    onStompError: frame => console.log(`Stomp client reported error: ${frame.headers['message']}. Details: ${frame.body}`),
    onDisconnect: frame => console.log(`Stomp client disconnected: ${frame.headers['message']}. Details: ${frame.body}`),
    debug: msg => console.log(msg),
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    reconnectDelay: 5000
});

const UserNotificationPanel = () => {
    const userState: UserState = useAppSelector(selectUserInfo);
    const userId = userState?.user?.id;
    const dispatch = useAppDispatch();

    // TODO JWT https://stomp-js.github.io/faqs/faqs.html#p-can-i-use-token-based-authentication-with-these-libraries-p
    useEffect(() => {
        client.activate();
        client.onConnect = frame => {
            console.log(`Stomp client connected: ${frame.headers['message']}. Details: ${frame.body}`);
            let stompSubscription = client.subscribe(`/topic/emailConfirmed/${userId}`, message => {
                console.log(`Received: ${JSON.parse(message.body)}`);
                client.unsubscribe(stompSubscription.id)
                dispatch(removeNotification(CONFIRM_EMAIL_NOTIFICATION));
                dispatch(addNotification(CONFIRM_EMAIL_SUCCESS_NOTIFICATION));
            });
        };
        return () => {
            client.deactivate().then();
        };
    }, [dispatch, userId]);

    const { notifications } = useAppSelector(selectNotificationsState);

    return (
        <Container>
            {
                notifications.map((notification, i) =>
                    <Alert key={i} variant={"warning"} transition={Fade} dismissible={true}
                           onClose={() => dispatch(removeNotification(CONFIRM_EMAIL_SUCCESS_NOTIFICATION))}>
                        {notification}
                    </Alert>
                )
            }
        </Container>
    );
};

export default UserNotificationPanel;