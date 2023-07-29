import {UserUpdateState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const userUpdateSlice = createSlice({
    name: 'userUpdate',
    initialState: { } as UserUpdateState,
    reducers: {
        userProfileByIdUpdateRequest: (state) => {
            return { loading: true };
        },
        userProfileByIdUpdateSuccess: () => {
            return { success: true };
        },
        userProfileByIdUpdateFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        userProfileByIdUpdateReset: () => {
            return { };
        }
    }
});

export const {
    userProfileByIdUpdateRequest,
    userProfileByIdUpdateSuccess,
    userProfileByIdUpdateFail,
    userProfileByIdUpdateReset
} = userUpdateSlice.actions;

export const selectUserUpdate: (state: RootState) => UserUpdateState = state => state.userUpdate;

export const userUpdateReducer = userUpdateSlice.reducer;