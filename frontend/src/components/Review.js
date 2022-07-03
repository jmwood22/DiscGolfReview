import React from "react";

export const Review = ({review}) => {

    return (
        <div className="row">
            <div className="col-md-12">
                <strong>{review.author.nickname}</strong>{' '}
                <span>
                    {review.rating}/5
                </span>
                <p>{review.text}</p>
            </div>
        </div>
    )
}