import {OrderDetail, OrderListState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const orderUserListSlice = createSlice({
    name: 'orderUserList',
    initialState: { orders: [] } as OrderListState,
    reducers: {
        orderUserListRequest: () => {
            return { loading: true };
        },
        orderUserListSuccess: (state, action: PayloadAction<OrderDetail[]>) => {
            return { orders: action.payload };
        },
        orderUserListFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        orderUserListReset: () => {
            return { orders: [] };
        }
    }
});

export const { orderUserListRequest, orderUserListSuccess, orderUserListFail, orderUserListReset } = orderUserListSlice.actions;


export const selectOrderUserList: (state: RootState) => OrderListState = state => state.orderUserList;

export const orderUserListReducer = orderUserListSlice.reducer;