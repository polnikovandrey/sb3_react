import {NotificationsState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const notificationsSlice = createSlice({
    name: 'notificationsList',
    initialState: { notifications: [] } as NotificationsState,
    reducers: {
        addNotification: (state, action: PayloadAction<string>) => {
            let notification = action.payload;
            let notifications = state.notifications;
            let newNotifications = [];
            newNotifications.push(notification);
            notifications.forEach(aNotification => newNotifications.push(aNotification));
            return { notifications: newNotifications };
        },
        removeNotification: (state, action: PayloadAction<string>) => {
            let notification = action.payload;
            let notifications = state.notifications;
            let newNotifications = notifications.filter(aNotification => aNotification === notification);
            return { notifications: newNotifications };
        },
        clearNotifications: () => {
            return { notifications: [] };
        }
    }
});

export const {
    addNotification, removeNotification, clearNotifications
} = notificationsSlice.actions;


export const selectNotificationsState: (state: RootState) => NotificationsState = state => state.notifications;

export const notificationsReducer = notificationsSlice.reducer;