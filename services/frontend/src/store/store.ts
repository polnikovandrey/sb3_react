import {configureStore} from "@reduxjs/toolkit";
import {EnhancedStore} from "@reduxjs/toolkit/src/configureStore";
import {UserState} from "./types";
import {userReducer} from "../slice/userSlice";
import {userProfileReducer} from "../slice/userProfileSlice";
import {userListReducer} from "../slice/userListSlice";
import {userDeleteReducer} from "../slice/userDeleteSlice";
import {userUpdateReducer} from "../slice/userUpdateSlice";
import {notificationsReducer} from "../slice/notificationsSlice";

const userLocalStorageItem = localStorage.getItem('user');
const userFromStorage: UserState = userLocalStorageItem ? { user: JSON.parse(userLocalStorageItem) } : { };

const store: EnhancedStore = configureStore(
    {
        reducer: {
            notifications: notificationsReducer,
            userInfo: userReducer,
            userList: userListReducer,
            userDelete: userDeleteReducer,
            userProfile: userProfileReducer,
            userUpdate: userUpdateReducer
        },
        preloadedState: {
            userInfo: userFromStorage
        }
    }
);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;