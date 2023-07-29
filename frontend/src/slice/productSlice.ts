import {ProductItem, ProductListLoadResultDto, ProductsDetailsState, ProductsListState} from "../store/types";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "../store/store";

export const productListSlice = createSlice({
    name: 'productList',
    initialState: {} as ProductsListState,
    reducers: {
        productListRequest: () => {
            return { loading: true };
        },
        productListSuccess: (state, action: PayloadAction<ProductListLoadResultDto>) => {
            return { loading: false, result: action.payload };
        },
        productListFail: (state, action: PayloadAction<string>) => {
            return { loading: false, error: action.payload };
        }
    }
});

export const { productListRequest, productListSuccess, productListFail } = productListSlice.actions;


export const selectProductList: (state: RootState) => ProductsListState = state => state.productList;

export const productListReducer = productListSlice.reducer;


export const productDetailSlice = createSlice({
    name: 'productDetail',
    initialState: {} as ProductsDetailsState,
    reducers: {
        productDetailRequest: () => {
            return { loading: true };
        },
        productDetailSuccess: (state, action: PayloadAction<ProductItem>) => {
            return { loading: false, item: action.payload };
        },
        productDetailFail: (state, action: PayloadAction<string>) => {
            return { loading: false, error: action.payload };
        }
    }
});

export const { productDetailRequest, productDetailSuccess, productDetailFail } = productDetailSlice.actions;


export const selectProductDetail: (state: RootState) => ProductsDetailsState = state => state.productDetails;

export const productDetailReducer = productDetailSlice.reducer;