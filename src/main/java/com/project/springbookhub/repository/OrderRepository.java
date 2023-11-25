package com.project.springbookhub.repository;

import com.project.springbookhub.model.concretes.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

}
