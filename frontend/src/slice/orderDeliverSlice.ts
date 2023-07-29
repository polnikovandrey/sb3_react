import {OrderDeliverState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const orderDeliverSlice = createSlice({
    name: 'orderDeliver',
    initialState: {} as OrderDeliverState,
    reducers: {
        orderDeliverRequest: () => {
            return { loading: true };
        },
        orderDeliverSuccess: () => {
            return { success: true };
        },
        orderDeliverFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        orderDeliverReset: () => {
            return {};
        }
    }
});

export const { orderDeliverRequest, orderDeliverSuccess, orderDeliverFail, orderDeliverReset } = orderDeliverSlice.actions;


export const selectOrderDeliver: (state: RootState) => OrderDeliverState = state => state.orderDeliver;

export const orderDeliverReducer = orderDeliverSlice.reducer;