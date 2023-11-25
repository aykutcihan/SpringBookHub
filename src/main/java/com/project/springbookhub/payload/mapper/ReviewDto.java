package com.project.springbookhub.payload.mapper;

import com.project.springbookhub.model.concretes.Review;
import com.project.springbookhub.payload.request.ReviewRequest;
import com.project.springbookhub.payload.response.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewDto {

    public Review mapReviewRequestToReview(ReviewRequest reviewRequest) {
        return Review.builder()
                .reviewDate(reviewRequest.getReviewDate())
                .book(reviewRequest.getBook())
                .content(reviewRequest.getContent())
                .client(reviewRequest.getClient())
                .build();
    }

    public ReviewResponse mapReviewToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .book(review.getBook())
                .content(review.getContent())
                .client(review.getClient())
                .reviewDate(review.getReviewDate())
                .build();
    }

    public void updateReviewFromReviewRequest(ReviewRequest reviewRequest, Review review) {

        review.setReviewDate(reviewRequest.getReviewDate());
        review.setClient(reviewRequest.getClient());
        review.setBook(reviewRequest.getBook());
        review.setContent(reviewRequest.getContent());


    }
}
