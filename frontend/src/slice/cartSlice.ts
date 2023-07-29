import {CartItem, CartState, ShippingAddress} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const cartSlice = createSlice({
    name: 'cart',
    initialState: { items: [], shippingAddress: {} as ShippingAddress, paymentMethod: 'PayPal' } as CartState,
    reducers: {
        addCartItem: (state, action: PayloadAction<CartItem>) => {
            const actionItem: CartItem = action.payload;
            const stateItem = state.items.find(item => item.productId === actionItem.productId);
            if (stateItem) {
                state.items = state.items.map(item => item.productId === stateItem.productId ? actionItem : item);
            } else {
                state.items = state.items.concat(actionItem);
            }
        },
        removeCartItem: (state, action: PayloadAction<string>) => {
            const productId: string = action.payload;
            state.items = state.items.filter(item => item.productId !== productId);
        },
        resetCartItems: (state) => {
            state.items = [];
        },
        saveShippingAddress: (state, action: PayloadAction<ShippingAddress>) => {
            state.shippingAddress = action.payload;
        },
        savePaymentMethod: (state, action: PayloadAction<string>) => {
            state.paymentMethod = action.payload;
        }
    }
});

export const { addCartItem, removeCartItem, resetCartItems, saveShippingAddress, savePaymentMethod } = cartSlice.actions;


export const selectCart: (state: RootState) => CartState = state => state.cart;

export const cartReducer = cartSlice.reducer;