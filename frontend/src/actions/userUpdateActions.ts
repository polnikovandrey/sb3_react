import {Dispatch} from "redux";
import axios, {AxiosRequestConfig} from "axios";
import {UserInfo, UserProfile} from "../store/types";
import {userProfileByIdUpdateFail, userProfileByIdUpdateRequest, userProfileByIdUpdateReset, userProfileByIdUpdateSuccess} from "../slice/userUpdateSlice";
import {userProfileReset, userProfileUpdateSuccess} from "../slice/userProfileSlice";

export const updateUserProfileByIdAction = async (token: string, aUserProfile: UserProfile, dispatch: Dispatch) => {
    try {
        dispatch(userProfileByIdUpdateRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: UserInfo } = await axios.put(`/api/users/${aUserProfile.id}`, aUserProfile, config);
        const userProfile: UserProfile = { id: data.id, name: data.name, email: data.email, admin: data.admin };
        dispatch(userProfileByIdUpdateSuccess());
        dispatch(userProfileUpdateSuccess(userProfile));
    } catch (error: any) {
        dispatch(userProfileByIdUpdateFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const resetUserProfileByIdAction = async (dispatch: Dispatch) => {
    dispatch(userProfileByIdUpdateReset());
    dispatch(userProfileReset())
};