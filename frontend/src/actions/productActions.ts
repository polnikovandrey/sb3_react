import axios, {AxiosRequestConfig} from "axios";
import {Dispatch} from "redux";
import {productDetailFail, productDetailRequest, productDetailSuccess, productListFail, productListRequest, productListSuccess} from "../slice/productSlice";
import {CreateReviewDto, ProductItem, ProductItemBase, ProductListLoadResultDto} from "../store/types";
import {productDeleteFail, productDeleteRequest, productDeleteSuccess} from "../slice/productDeleteSlice";
import {productCreateFail, productCreateRequest, productCreateReset, productCreateSuccess} from "../slice/productCreateSlice";
import {productUpdateFail, productUpdateRequest, productUpdateReset, productUpdateSuccess} from "../slice/productUpdateSlice";
import {reviewCreateFail, reviewCreateRequest, reviewCreateReset, reviewCreateSuccess} from "../slice/reviewCreateSlice";
import {productTopFail, productTopRequest, productTopSuccess} from "../slice/productTopSlice";

export const loadProductListAction = async (dispatch: Dispatch, keyword: string = '', pageNumber = '') => {
    try {
        dispatch(productListRequest());
        const { data }: { data: ProductListLoadResultDto } = await axios.get(`/api/product?keyword=${keyword}&pageNumber=${pageNumber}`);
        dispatch(productListSuccess(data));
    } catch (error: any) {
        dispatch(productListFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};

export const loadProductDetailsAction = async (productId: string, dispatch: Dispatch) => {
    try {
        dispatch(productDetailRequest());
        const { data: product }: { data: ProductItem } = await axios.get(`/api/product/${productId}`);
        dispatch(productDetailSuccess(product));
    } catch (error: any) {
        const errorMessage: string = error.response && error.response.data.message ? error.response.data.message : error.message;
        dispatch(productDetailFail(errorMessage));
    }
};

export const deleteProductAction = async (productId: string, token: string, dispatch: Dispatch) => {
    try {
        dispatch(productDeleteRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };
        await axios.delete(`/api/product/${productId}`, config);
        dispatch(productDeleteSuccess());
    } catch (error: any) {
        const errorMessage: string = error.response && error.response.data.message ? error.response.data.message : error.message;
        dispatch(productDeleteFail(errorMessage));
    }
};

export const createProductAction = async (token: string, dispatch: Dispatch) => {
    try {
        dispatch(productCreateRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };
        const { data: product }: { data: ProductItem } = await axios.post('/api/product', {}, config);
        dispatch(productCreateSuccess(product));
    } catch (error: any) {
        const errorMessage: string = error.response && error.response.data.message ? error.response.data.message : error.message;
        dispatch(productCreateFail(errorMessage));
    }
};

export const resetCreateProductAction = (dispatch: Dispatch) => {
    dispatch(productCreateReset());
}

export const updateProductAction = async (product: ProductItemBase, token: string, dispatch: Dispatch) => {
    try {
        dispatch(productUpdateRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };
        const { data: updatedProduct }: { data: ProductItem } = await axios.put(`/api/product/${product._id}`, product, config);
        dispatch(productDetailSuccess(updatedProduct));
        dispatch(productUpdateSuccess(updatedProduct));
    } catch (error: any) {
        const errorMessage: string = error.response && error.response.data.message ? error.response.data.message : error.message;
        dispatch(productUpdateFail(errorMessage));
    }
};

export const resetUpdateProductAction = (dispatch: Dispatch) => {
    dispatch(productUpdateReset());
}

export const createReviewAction = async (productId: string, review: CreateReviewDto, token: string, dispatch: Dispatch) => {
    try {
        dispatch(reviewCreateRequest());
        const config: AxiosRequestConfig = {
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };
        await axios.post(`/api/product/${productId}/review`, review , config);
        dispatch(reviewCreateSuccess());
    } catch (error: any) {
        const errorMessage: string = error.response && error.response.data.message ? error.response.data.message : error.message;
        dispatch(reviewCreateFail(errorMessage));
    }
};

export const resetCreateReviewAction = (dispatch: Dispatch) => {
    dispatch(reviewCreateReset());
}

export const loadProductTopAction = async (dispatch: Dispatch) => {
    try {
        dispatch(productTopRequest());
        const { data }: { data: ProductItem[] } = await axios.get('/api/product/top');
        dispatch(productTopSuccess(data));
    } catch (error: any) {
        dispatch(productTopFail(error.response && error.response.data.message ? error.response.data.message : error.message));
    }
};