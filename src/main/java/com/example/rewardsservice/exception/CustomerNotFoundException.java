package com.example.rewardsservice.exception;

public class CustomerNotFoundException extends RuntimeException{

    public  CustomerNotFoundException(String message){
        super(message);
    }

}
