import {UserDeleteStata} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const userDeleteSlice = createSlice({
    name: 'userDelete',
    initialState: { } as UserDeleteStata,
    reducers: {
        userDeleteRequest: () => {
            return { loading: true };
        },
        userDeleteSuccess: () => {
            return { success: true };
        },
        userDeleteFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
    }
});

export const { userDeleteRequest, userDeleteSuccess, userDeleteFail } = userDeleteSlice.actions;

export const selectUserDelete: (state: RootState) => UserDeleteStata = state => state.userDelete;

export const userDeleteReducer = userDeleteSlice.reducer;