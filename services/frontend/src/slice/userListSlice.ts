import {UserListPage, UserListState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const userListSlice = createSlice({
    name: 'userList',
    initialState: { } as UserListState,
    reducers: {
        userListRequest: () => {
            return { loading: true };
        },
        userListSuccess: (state, action: PayloadAction<UserListPage>) => {
            return { users: action.payload };
        },
        userListFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        userListReset: () => {
            return { };
        }
    }
});

export const { userListRequest, userListSuccess, userListFail, userListReset } = userListSlice.actions;

export const selectUserList: (state: RootState) => UserListState = state => state.userList;

export const userListReducer = userListSlice.reducer;