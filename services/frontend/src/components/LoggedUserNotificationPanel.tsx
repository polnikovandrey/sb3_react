import {Client} from "@stomp/stompjs";
import {useEffect} from "react";
import {Container} from "react-bootstrap";
import {NotificationsState} from "../store/types";
import {useAppSelector} from "../store/hooks";
import {selectNotificationsState} from "../slice/notificationsSlice";

const client = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    onChangeState: state => console.log(`Stomp client state changed: ${state}`),
    onConnect: frame => console.log(`Stomp client connected: ${frame.headers['message']}. Details: ${frame.body}`),
    onDisconnect: frame => console.log(`Stomp client disconnected: ${frame.headers['message']}. Details: ${frame.body}`),
    onStompError: frame => console.log(`Stomp client reported error: ${frame.headers['message']}. Details: ${frame.body}`),
    debug: msg => console.log(msg),
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    reconnectDelay: 5000
});

const LoggedUserNotificationPanel = () => {
    // TODO
    // let confirmEmailSubscription = client.subscribe('/topic/emailConfirmed', message => {      // TODO user/{recipientId}/topic/emailConfirmed
    //     console.log(`Received: ${JSON.parse(message.body)}`);
    //     dispatch(removeNotification(CONFIRM_EMAIL_NOTIFICATION));
    // });
    // client.unsubscribe(confirmEmailSubscription.id);

    // TODO JWT https://stomp-js.github.io/faqs/faqs.html#p-can-i-use-token-based-authentication-with-these-libraries-p
    useEffect(() => {
        client.activate();
        return () => {
            client.deactivate().then();
        };
    }, []);

    const notificationsState: NotificationsState = useAppSelector(selectNotificationsState);
    useEffect(() => {

    }, [notificationsState]);
    return (
        <Container>
            {
                notificationsState.notifications.map(notification => <div>{notification}</div>)
            }
        </Container>
    );
};

export default LoggedUserNotificationPanel;