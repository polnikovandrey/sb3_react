import React from "react";
import {ProductRatingItem} from "../store/types";

// www.fontawesome.com styles are being used to draw stars
export const Rating = ({ ratingItem, color }: { ratingItem: ProductRatingItem, color: string }) => {
    return (
        <div className='rating'>
            <span>
                <i style={{ color }}
                   className={
                       ratingItem.rating >= 1
                           ? 'fas fa-star'
                           : ratingItem.rating > 0.5
                               ? 'fas fa-star-half-alt'
                               : 'far fa-star'}/>
                <i style={{ color }}
                   className={
                       ratingItem.rating >= 2
                           ? 'fas fa-star'
                           : ratingItem.rating > 1.5
                               ? 'fas fa-star-half-alt'
                               : 'far fa-star'}/>
                <i style={{ color }}
                   className={
                       ratingItem.rating >= 3
                           ? 'fas fa-star'
                           : ratingItem.rating > 2.5
                               ? 'fas fa-star-half-alt'
                               : 'far fa-star'}/>
                <i style={{ color }}
                   className={
                       ratingItem.rating >= 4
                           ? 'fas fa-star'
                           : ratingItem.rating > 3.5
                               ? 'fas fa-star-half-alt'
                               : 'far fa-star'}/>
                <i style={{ color }}
                   className={
                       ratingItem.rating >= 5
                           ? 'fas fa-star'
                           : ratingItem.rating > 4.5
                               ? 'fas fa-star-half-alt'
                               : 'far fa-star'}/>
            </span>
            { ratingItem.numReviews && <span>{ratingItem.numReviews} reviews</span> }
        </div>
    );
}

Rating.defaultProps = {
    color: '#f8e825'
}