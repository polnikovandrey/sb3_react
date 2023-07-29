import {OrderDetail, OrderListState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const orderListSlice = createSlice({
    name: 'orderList',
    initialState: { orders: [] } as OrderListState,
    reducers: {
        orderListRequest: () => {
            return { loading: true };
        },
        orderListSuccess: (state, action: PayloadAction<OrderDetail[]>) => {
            return { orders: action.payload };
        },
        orderListFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        }
    }
});

export const { orderListRequest, orderListSuccess, orderListFail } = orderListSlice.actions;


export const selectOrderList: (state: RootState) => OrderListState = state => state.orderList;

export const orderListReducer = orderListSlice.reducer;