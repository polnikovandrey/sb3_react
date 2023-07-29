import {ProductItem, ProductsTopState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const productTopSlice = createSlice({
    name: 'productTop',
    initialState: {} as ProductsTopState,
    reducers: {
        productTopRequest: () => {
            return { loading: true };
        },
        productTopSuccess: (state, action: PayloadAction<ProductItem[]>) => {
            return { loading: false, products: action.payload };
        },
        productTopFail: (state, action: PayloadAction<string>) => {
            return { loading: false, error: action.payload };
        }
    }
});

export const { productTopRequest, productTopSuccess, productTopFail } = productTopSlice.actions;


export const selectProductTop: (state: RootState) => ProductsTopState = state => state.productTop;

export const productTopReducer = productTopSlice.reducer;