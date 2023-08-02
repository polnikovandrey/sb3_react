import {Dispatch} from "redux";
import axios, {AxiosRequestConfig} from "axios";
import {UserInfo, UserProfile} from "../store/types";
import {
    userProfileFail,
    userProfileRequest,
    userProfileSuccess,
    userProfileUpdateFail,
    userProfileUpdateRequest,
    userProfileUpdateReset,
    userProfileUpdateSuccess
} from "../slice/userProfileSlice";

export const getUserProfileAction = async (id: string, token: string, dispatch: Dispatch) => {
    try {
        dispatch(userProfileRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: UserInfo } = await axios.get(`/api/users/${id}`, config);
        const userProfile: UserProfile = { id: data.id, name: data.name, email: data.email, admin: data.admin };
        dispatch(userProfileSuccess(userProfile));
    } catch (error: any) {
        dispatch(userProfileFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const updateUserProfileAction = async (token: string, aUserProfile: UserProfile, dispatch: Dispatch) => {
    try {
        dispatch(userProfileUpdateRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: UserInfo } = await axios.put(`/api/users/profile`, aUserProfile, config);
        const userProfile: UserProfile = { name: data.name, email: data.email };
        dispatch(userProfileUpdateSuccess(userProfile));
    } catch (error: any) {
        dispatch(userProfileUpdateFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const updateUserProfileResetAction = async (dispatch: Dispatch) => {
    dispatch(userProfileUpdateReset());
};