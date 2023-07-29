import {OrderPayState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const orderPaySlice = createSlice({
    name: 'orderPay',
    initialState: {} as OrderPayState,
    reducers: {
        orderPayRequest: () => {
            return { loading: true };
        },
        orderPaySuccess: () => {
            return { success: true };
        },
        orderPayFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        orderPayReset: () => {
            return {};
        }
    }
});

export const { orderPayRequest, orderPaySuccess, orderPayFail, orderPayReset } = orderPaySlice.actions;


export const selectOrderPay: (state: RootState) => OrderPayState = state => state.orderPay;

export const orderPayReducer = orderPaySlice.reducer;