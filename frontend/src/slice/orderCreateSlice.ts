import {Order, OrderState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const orderCreateSlice = createSlice({
    name: 'orderCreate',
    initialState: {} as OrderState,
    reducers: {
        orderCreateRequest: () => {
            return { loading: true };
        },
        orderCreateSuccess: (state, action: PayloadAction<Order>) => {
            return { order: action.payload };
        },
        orderCreateFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        orderCreateReset: () => {
            return {};
        }
    }
});

export const { orderCreateRequest, orderCreateSuccess, orderCreateFail, orderCreateReset } = orderCreateSlice.actions;

export const selectOrderCreate: (state: RootState) => OrderState = state => state.orderCreate;

export const orderCreateReducer = orderCreateSlice.reducer;