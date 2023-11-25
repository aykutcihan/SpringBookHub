package com.project.springbookhub.repository;

import com.project.springbookhub.model.concretes.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
