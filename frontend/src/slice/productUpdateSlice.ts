import {ProductItem, ProductUpdateState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const productUpdateSlice = createSlice({
    name: 'productUpdate',
    initialState: { } as ProductUpdateState,
    reducers: {
        productUpdateRequest: () => {
            return { loading: true };
        },
        productUpdateSuccess: (state, action: PayloadAction<ProductItem>) => {
            return { product: action.payload, success: true };
        },
        productUpdateFail: (state, action: PayloadAction<string>) => {
            return { error: action.payload };
        },
        productUpdateReset: () => {
            return { };
        }
    }
});

export const { productUpdateRequest, productUpdateSuccess, productUpdateFail, productUpdateReset } = productUpdateSlice.actions;

export const selectProductUpdate: (state: RootState) => ProductUpdateState = state => state.productUpdate;

export const productUpdateReducer = productUpdateSlice.reducer;