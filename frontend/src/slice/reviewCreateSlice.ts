import {ReviewCreateState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const reviewCreateSlice = createSlice({
    name: 'reviewCreate',
    initialState: { } as ReviewCreateState,
    reducers: {
        reviewCreateRequest: () => {
            return { loading: true };
        },
        reviewCreateSuccess: () => {
            return { success: true };
        },
        reviewCreateFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        reviewCreateReset: () => {
            return { };
        }
    }
});

export const { reviewCreateRequest, reviewCreateSuccess, reviewCreateFail, reviewCreateReset } = reviewCreateSlice.actions;

export const selectReviewCreate: (state: RootState) => ReviewCreateState = state => state.reviewCreate;

export const reviewCreateReducer = reviewCreateSlice.reducer;