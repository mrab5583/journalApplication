package com.edigest.myFirstProject.exception;

public class ResourceNotFoundException extends RuntimeException{

        public ResourceNotFoundException(String message){
            super(message);
        }
}
