package com.example.rewardsservice.exception;


import com.example.rewardsservice.entity.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RewardsAdvice {


    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Customer> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(RewardsException.class)
    public ResponseEntity<Customer> handleRewardsException(RewardsException ex) {
        return ResponseEntity.badRequest().build();
    }
}
