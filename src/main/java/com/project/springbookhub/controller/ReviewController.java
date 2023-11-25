package com.project.springbookhub.controller;

import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.request.OrderRequest;
import com.project.springbookhub.payload.request.ReviewRequest;
import com.project.springbookhub.payload.response.OrderResponse;
import com.project.springbookhub.payload.response.ReviewResponse;
import com.project.springbookhub.service.OrderService;
import com.project.springbookhub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<ReviewResponse> createOrder (@RequestBody @Valid ReviewRequest reviewRequest) {
        return reviewService.createReview(reviewRequest);
    }

    @GetMapping("/getReviewById/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<ReviewResponse> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/getAllReview")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ReviewResponse> getAllReview() {
        return reviewService.getAllReview();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<ReviewResponse> updateReview(@PathVariable Long id,
                                                      @RequestBody @Valid ReviewRequest reviewRequest) {
        return reviewService.updateReview(id, reviewRequest);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<?> deleteReview (@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }
}
