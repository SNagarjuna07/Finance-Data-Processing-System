package com.finance.dashboard.excpetion;

public class InactiveUserException extends RuntimeException {

    public InactiveUserException(String msg) {
        super(msg);
    }
}
