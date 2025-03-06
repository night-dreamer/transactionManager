package com.beiji.transactionmanager.model.exception;

/**
 * Exception for Transaction
 */
public class TransactionErrorException extends RuntimeException {
    public TransactionErrorException(String message) {
        super(message);
    }
}
