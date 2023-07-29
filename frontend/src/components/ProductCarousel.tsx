import React, {useEffect} from 'react';
import {Link} from 'react-router-dom';
import {useAppDispatch, useAppSelector} from "../store/hooks";
import {selectProductTop} from "../slice/productTopSlice";
import {loadProductTopAction} from "../actions/productActions";
import Message from "./Message";
import {Carousel, Image} from "react-bootstrap";

const ProductCarousel = () => {
    const dispatch = useAppDispatch();
    const { products, error } = useAppSelector(selectProductTop);
    useEffect(() => {
        (async () => {
            await loadProductTopAction(dispatch);

        })();
    }, [ dispatch ]);
    return (
        <>
            { error && <Message variant='danger'>{error}</Message> }
            { products
                && (
                    <Carousel pause='hover' indicators={false} nextLabel={null} prevLabel={null} className='bg-dark'>
                        {
                            products.map(product => (
                                <Carousel.Item key={product._id}>
                                    <Link to={`/product/${product._id}`}>
                                        <Image src={product.image} alt={product.name} fluid/>
                                        <Carousel.Caption className='carousel-caption'>
                                            <h2>{product.name} (${product.price})</h2>
                                        </Carousel.Caption>
                                    </Link>
                                </Carousel.Item>
                            ))
                        }
                    </Carousel>
                )
            }
        </>
    );
};

export default ProductCarousel;