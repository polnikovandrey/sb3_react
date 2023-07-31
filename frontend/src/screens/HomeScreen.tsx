import React, {useEffect} from "react";
import {Link} from 'react-router-dom';
import {Helmet} from "react-helmet-async";
import {useParams} from "react-router";
import {Col, Row} from "react-bootstrap";
import Product from "../components/Product";
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {loadProductListAction} from "../actions/productActions";
import Loader from "../components/Loader";
import Message from "../components/Message";
import {ProductItem} from "../store/types";
import {selectProductList} from "../slice/productSlice";
import Paginate from "../components/Paginate";
import ProductCarousel from "../components/ProductCarousel";
import Meta from "../components/Meta";
import {selectProductTop} from "../slice/productTopSlice";

const HomeScreen = () => {
    const { keyword: aKeyword, pageNumber: aPageNumber } = useParams();
    const keyword = aKeyword || '';
    const pageNumber = aPageNumber || String(1);
    const productList = useAppSelector(selectProductList);
    const { loading: productListLoading, result: productListLoadResult, error: productListLoadError } = productList;
    const { products, pages, page } = productListLoadResult || { };
    const { loading: productTopLoading  } = useAppSelector(selectProductTop);
    const dispatch = useAppDispatch();

    useEffect(() => {
        (async () => {
            await loadProductListAction(dispatch, keyword, pageNumber);
        })();
    }, [ dispatch, keyword, pageNumber ]);

    return (
        <>
            <Meta/>
            <Helmet>
                <title>Welcome to ProShop</title>
                <meta name='description' content='We sell best products for cheep'/>
                <meta name='keywords' content='electronics, buy electronics, cheap electronics'/>
            </Helmet>
            { !keyword
                ? <ProductCarousel/>
                : <Link to='/' className='btn btn-light'>Go back</Link>
            }
            <h1>Latest Products</h1>
            { ( productListLoading || productTopLoading) && <Loader/> }
            { productListLoadError && <Message variant='danger'>{productListLoadError}</Message> }
            { productListLoadResult && (
                <>
                    <Row>
                        {
                            products
                                ? products.map((productData: ProductItem) => {
                                    return <Col key={productData._id} sm={12} md={6} lg={4} xl={3}>
                                        <Product productItem={productData}/>
                                    </Col>
                                })
                                : <h3>Empty</h3>
                        }
                    </Row>
                    <Paginate pages={pages!} page={page!} admin={false} keyword={keyword ? keyword : ''}/>
                </>
            )
            }
        </>
    )
};
export default HomeScreen;