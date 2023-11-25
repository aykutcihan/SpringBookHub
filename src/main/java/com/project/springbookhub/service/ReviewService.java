package com.project.springbookhub.service;

import com.project.springbookhub.exception.OrderNotFoundException;
import com.project.springbookhub.exception.ReviewNotFoundException;
import com.project.springbookhub.model.concretes.Order;
import com.project.springbookhub.model.concretes.Review;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.mapper.OrderDto;
import com.project.springbookhub.payload.mapper.ReviewDto;
import com.project.springbookhub.payload.request.OrderRequest;
import com.project.springbookhub.payload.request.ReviewRequest;
import com.project.springbookhub.payload.response.OrderResponse;
import com.project.springbookhub.payload.response.ReviewResponse;
import com.project.springbookhub.repository.OrderRepository;
import com.project.springbookhub.repository.ReviewRepository;
import com.project.springbookhub.util.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewDto reviewDto;

    public ResponseMessage<ReviewResponse> createReview(ReviewRequest reviewRequest) {

        Review review = reviewRepository.save(reviewDto.mapReviewRequestToReview(reviewRequest));
        ReviewResponse reviewResponse = reviewDto.mapReviewToReviewResponse(review);

        return ResponseMessage.<ReviewResponse>builder()
                .object(reviewResponse)
                .message("Review Created Successfully")
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<ReviewResponse> getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(String.format(Messages.REVIEW_NOT_FOUND,id)));

        ReviewResponse reviewResponse = reviewDto.mapReviewToReviewResponse(review);

        return ResponseMessage.<ReviewResponse>builder()
                .object(reviewResponse)
                .message("Review found")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<ReviewResponse> getAllReview() {

        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(reviewDto::mapReviewToReviewResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<ReviewResponse> updateReview(Long id, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(String.format(Messages.REVIEW_NOT_FOUND,id)));

        reviewDto.updateReviewFromReviewRequest(reviewRequest,review);
        Review updatedReview = reviewRepository.save(review);

        return ResponseMessage.<ReviewResponse>builder()
                .object(reviewDto.mapReviewToReviewResponse(updatedReview))
                .message("Review updated successfully")
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<?> deleteReview(Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(String.format(Messages.REVIEW_NOT_FOUND,id)));

        reviewRepository.deleteById(review.getId());

        return ResponseMessage.<ReviewResponse>builder()
                .message("Review deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
