import {useEffect} from "react";
import {Container} from "react-bootstrap";
import {NotificationsState, UserState} from "../store/types";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {addNotification, removeNotification, selectNotificationsState} from "../slice/notificationsSlice";
import {Client, StompSubscription} from "@stomp/stompjs";
import {CONFIRM_EMAIL_NOTIFICATION, CONFIRM_EMAIL_SUCCESS_NOTIFICATION} from "../notifications";
import {selectUserInfo} from "../slice/userSlice";

const client = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    onChangeState: state => console.log(`Stomp client state changed: ${state}`),
    onStompError: frame => console.log(`Stomp client reported error: ${frame.headers['message']}. Details: ${frame.body}`),
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
        let subscriptions: StompSubscription[] = [];
        client.activate();
        client.onConnect = frame => {
            console.log(`Stomp client connected: ${frame.headers['message']}. Details: ${frame.body}`);
            subscriptions.push(client.subscribe(`/topic/emailConfirmed/${userId}`, message => {
                console.log(`Received: ${JSON.parse(message.body)}`);
                dispatch(removeNotification(CONFIRM_EMAIL_NOTIFICATION));
                dispatch(addNotification(CONFIRM_EMAIL_SUCCESS_NOTIFICATION));
            }));
        };
        client.onDisconnect = frame => {
            console.log(`Stomp client disconnected: ${frame.headers['message']}. Details: ${frame.body}`);
            subscriptions.forEach(subscription => client.unsubscribe(subscription.id));
        };
        return () => {
            client.deactivate().then();
        };
    }, [dispatch, userId]);

    const notificationsState: NotificationsState = useAppSelector(selectNotificationsState);
    useEffect(() => {

    }, [notificationsState]);
    return (
        <Container>
            {
                notificationsState.notifications.map((notification, i) => <div key={i}>{notification}</div>)
            }
        </Container>
    );
};

export default UserNotificationPanel;