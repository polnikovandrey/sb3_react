import React from "react";
import {Link} from "react-router-dom";
import {Card} from "react-bootstrap";
import {Rating} from "./Rating";
import {ProductItem} from "../store/types";

const Product = ({ productItem }: { productItem: ProductItem }) => {
    return (
        <Card className='my-3 p-3 rounded'>
            <Link to={`/product/${productItem._id}`}>
                <Card.Img src={productItem.image} variant='top'/>
            </Link>
            <Card.Body>
                <Link to={`/product/${productItem._id}`}>
                    <Card.Title as='div'>
                        <strong>{productItem.name}</strong>
                    </Card.Title>
                </Link>
                <Card.Text as='div'>
                    <Rating ratingItem={{ rating: productItem.rating, numReviews: productItem.numReviews }}/>
                </Card.Text>
                <Card.Text as='h3'>${productItem.price}</Card.Text>
            </Card.Body>
        </Card>
    );
};
export default Product;