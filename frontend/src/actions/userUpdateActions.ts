import {Dispatch} from "redux";
import axios, {AxiosRequestConfig} from "axios";
import {UserProfile, UserProfileData} from "../store/types";
import {userProfileByIdUpdateFail, userProfileByIdUpdateRequest, userProfileByIdUpdateReset, userProfileByIdUpdateSuccess} from "../slice/userUpdateSlice";
import {userProfileReset, userProfileUpdateSuccess} from "../slice/userProfileSlice";
import {API_BASE_URL} from "../constants";

export const updateUserProfileByIdAction = async (token: string, aUserProfile: UserProfile, dispatch: Dispatch) => {
    try {
        dispatch(userProfileByIdUpdateRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: UserProfileData } = await axios.put(`${API_BASE_URL}/user/${aUserProfile.id}`, aUserProfile, config);
        const userProfile: UserProfile = { id: data.id, email: data.email, name: data.name, firstName: data.firstName, lastName: data.lastName, middleName: data.middleName, admin: data.admin };
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