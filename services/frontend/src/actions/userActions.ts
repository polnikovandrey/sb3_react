import {Dispatch} from "redux";
import {UserInfo, UserListPage} from "../store/types";
import {userLoginFail, userLoginRequest, userLoginSuccess, userLogout, userRegisterFail, userRegisterRequest, userRegisterSuccess} from "../slice/userSlice";
import axios, {AxiosRequestConfig} from "axios";
import {userListFail, userListRequest, userListReset, userListSuccess} from "../slice/userListSlice";
import {userProfileReset} from "../slice/userProfileSlice";
import {userDeleteFail, userDeleteRequest, userDeleteSuccess} from "../slice/userDeleteSlice";
import {API_BASE_URL} from "../constants";
import {addNotification, clearNotifications} from "../slice/notificationsSlice";
import {CONFIRM_EMAIL_NOTIFICATION} from "../notifications";

export const userLoginAction = async (usernameOrEmail: string, password: string, dispatch: Dispatch) => {
    try {
        dispatch(userLoginRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        const { data }: { data: UserInfo } = await axios.post(`${API_BASE_URL}/auth/signin`, { usernameOrEmail, password }, config);
        dispatch(userLoginSuccess(data));
        localStorage.setItem('user', JSON.stringify(data));
    } catch (error: any) {
        dispatch(userLoginFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const userLogoutAction = async (dispatch: Dispatch) => {
    localStorage.removeItem('user');
    dispatch(userLogout());
    dispatch(userProfileReset());
    dispatch(userListReset())
    dispatch(clearNotifications());
};

export const userRegisterAction = async (firstName: string, lastName: string, middleName: string, username: string, email: string, password: string, dispatch: Dispatch) => {
    try {
        dispatch(userRegisterRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        const { data }: { data: UserInfo } = await axios.post(`${API_BASE_URL}/auth/signup`, {firstName, lastName, middleName, username, email, password }, config);
        dispatch(userRegisterSuccess(data));
        dispatch(userLoginSuccess(data));
        localStorage.setItem('user', JSON.stringify(data));
        dispatch(addNotification(CONFIRM_EMAIL_NOTIFICATION));
    } catch (error: any) {
        dispatch(userRegisterFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const userListAction = async (page: number, token: string, dispatch: Dispatch) => {
    try {
        dispatch(userListRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: UserListPage } = await axios.get(`${API_BASE_URL}/user/list${page === 0 ? '' : ('/' + page.toString())}`, config);
        dispatch(userListSuccess(data));
    } catch (error: any) {
        dispatch(userListFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const userDeleteAction = async (id: number, token: string, dispatch: Dispatch) => {
    try {
        dispatch(userDeleteRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };
        await axios.delete(`${API_BASE_URL}/user/${id}`, config);
        dispatch(userDeleteSuccess());
    } catch (error: any) {
        dispatch(userDeleteFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};