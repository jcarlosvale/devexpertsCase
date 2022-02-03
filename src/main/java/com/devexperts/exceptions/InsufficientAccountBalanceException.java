package com.devexperts.exceptions;

public class InsufficientAccountBalanceException extends RuntimeException {

    public InsufficientAccountBalanceException(String msg) {
        super(msg);
    }
}
