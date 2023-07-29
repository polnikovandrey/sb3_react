import axios from "axios";
import {Dispatch} from "redux";
import {addCartItem, removeCartItem, resetCartItems, savePaymentMethod, saveShippingAddress} from "../slice/cartSlice";
import store from "../store/store";
import {ProductItem, ShippingAddress} from "../store/types";

export const cartAddItemAction = async (id: string, quantity: number, dispatch: Dispatch) => {
    const { data }: { data: ProductItem } = await axios.get(`/api/product/${id}`);
    dispatch(addCartItem({ productId: data._id, name: data.name, image: data.image, price: data.price, countInStock: data.countInStock, quantity: quantity}));
    localStorage.setItem('cartItems', JSON.stringify(store.getState().cart.items));
};

export const cartRemoveItemAction = async (id: string, dispatch: Dispatch) => {
    dispatch(removeCartItem(id));
    localStorage.setItem('cartItems', JSON.stringify(store.getState().cart.items));
};

export const cartResetItemsAction = async (dispatch: Dispatch) => {
    dispatch(resetCartItems());
    localStorage.removeItem('cartItems');
};

export const saveShippingAddressAction = async (data: ShippingAddress, dispatch: Dispatch) => {
    dispatch(saveShippingAddress(data));
    localStorage.setItem('shippingAddress', JSON.stringify(data));
};

export const savePaymentMethodAction = async (data: string, dispatch: Dispatch) => {
    dispatch(savePaymentMethod(data));
    localStorage.setItem('paymentMethod', JSON.stringify(data));
};