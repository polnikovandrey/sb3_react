import axios, {AxiosRequestConfig} from "axios";
import {Dispatch} from "redux";
import {Order, OrderDetail, PaymentResult} from "../store/types";
import {orderCreateFail, orderCreateRequest, orderCreateReset, orderCreateSuccess} from "../slice/orderCreateSlice";
import {orderDetailFail, orderDetailRequest, orderDetailReset, orderDetailSuccess} from "../slice/orderDetailSlice";
import {orderPayFail, orderPayRequest, orderPayReset, orderPaySuccess} from "../slice/orderPaySlice";
import {orderUserListFail, orderUserListRequest, orderUserListSuccess} from "../slice/orderUserListSlice";
import {orderListFail, orderListRequest, orderListSuccess} from "../slice/orderListSlice";
import {orderDeliverFail, orderDeliverRequest, orderDeliverReset, orderDeliverSuccess} from "../slice/orderDeliverSlice";

export const orderCreateAction = async (order: Order, token: string, dispatch: Dispatch) => {
    try {
        dispatch(orderCreateRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: Order } = await axios.post('/api/orders', order, config);
        dispatch(orderCreateSuccess(data));
    } catch (error: any) {
        dispatch(orderCreateFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const orderCreateResetAction = async (dispatch: Dispatch) => {
    dispatch(orderCreateReset());
}

export const orderDetailAction = async (orderId: string, token: string, dispatch: Dispatch) => {
    try {
        dispatch(orderDetailRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: OrderDetail } = await axios.get(`/api/orders/${orderId}`, config);
        dispatch(orderDetailSuccess(data));
    } catch (error: any) {
        dispatch(orderDetailFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const orderDetailResetAction = async (dispatch: Dispatch) => {
    dispatch(orderDetailReset());
}

export const orderPayAction = async (orderId: string, paymentResult: PaymentResult, token: string, dispatch: Dispatch) => {
    try {
        dispatch(orderPayRequest());
        const config: AxiosRequestConfig = {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        };
        await axios.put(`/api/orders/${orderId}/paid`, paymentResult, config);
        dispatch(orderPaySuccess());
    } catch (error: any) {
        dispatch(orderPayFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const orderPayResetAction = async (dispatch: Dispatch) => {
    dispatch(orderPayReset());
};

export const orderDeliverAction = async (orderId: string, token: string, dispatch: Dispatch) => {
    try {
        dispatch(orderDeliverRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };
        await axios.put(`/api/orders/${orderId}/deliver`, { }, config);
        dispatch(orderDeliverSuccess());
    } catch (error: any) {
        dispatch(orderDeliverFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const orderDeliverResetAction = async (dispatch: Dispatch) => {
    dispatch(orderDeliverReset());
};

export const orderUserListAction = async (token: string, dispatch: Dispatch) => {
    try {
        dispatch(orderUserListRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };
        const { data }: { data: OrderDetail[] } = await axios.get('/api/orders/userOrderList', config);
        dispatch(orderUserListSuccess(data));
    } catch (error: any) {
        dispatch(orderUserListFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const orderListAction = async (token: string, dispatch: Dispatch) => {
    try {
        dispatch(orderListRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };
        const { data: orders }: { data: OrderDetail[] } = await axios.get('/api/orders', config);
        dispatch(orderListSuccess(orders));
    } catch (error: any) {
        dispatch(orderListFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};