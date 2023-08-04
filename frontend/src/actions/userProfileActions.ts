import {Dispatch} from "redux";
import axios, {AxiosRequestConfig} from "axios";
import {UserProfile, UserProfileData} from "../store/types";
import {
    userProfileFail,
    userProfileRequest,
    userProfileSuccess,
    userProfileUpdateFail,
    userProfileUpdateRequest,
    userProfileUpdateReset,
    userProfileUpdateSuccess
} from "../slice/userProfileSlice";
import {API_BASE_URL} from "../constants";

export const getUserProfileAction = async (id: number, token: string, dispatch: Dispatch) => {
    try {
        dispatch(userProfileRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: UserProfileData } = await axios.get(`${API_BASE_URL}/user/${id}`, config);
        const userProfile: UserProfile = { id: data.id, email: data.email, name: data.name, firstName: data.firstName, lastName: data.lastName, middleName: data.middleName, admin: data.admin };
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
        const { data }: { data: UserProfileData } = await axios.put(`${API_BASE_URL}/user/${aUserProfile.id}`, aUserProfile, config);
        const userProfile: UserProfile = { id: data.id, email: data.email, name: data.name, firstName: data.firstName, lastName: data.lastName, middleName: data.middleName, admin: data.admin };
        dispatch(userProfileUpdateSuccess(userProfile));
    } catch (error: any) {
        dispatch(userProfileUpdateFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const updateUserProfileResetAction = async (dispatch: Dispatch) => {
    dispatch(userProfileUpdateReset());
};