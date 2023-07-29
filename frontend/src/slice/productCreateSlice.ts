import {ProductCreateState, ProductItem} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const productCreateSlice = createSlice({
    name: 'productCreate',
    initialState: { } as ProductCreateState,
    reducers: {
        productCreateRequest: () => {
            return { loading: true };
        },
        productCreateSuccess: (state, action: PayloadAction<ProductItem>) => {
            return { product: action.payload, success: true };
        },
        productCreateFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        productCreateReset: () => {
            return { };
        }
    }
});

export const { productCreateRequest, productCreateSuccess, productCreateFail, productCreateReset } = productCreateSlice.actions;

export const selectProductCreate: (state: RootState) => ProductCreateState = state => state.productCreate;

export const productCreateReducer = productCreateSlice.reducer;