package com.beiji.transactionmanager.service;

import com.beiji.transactionmanager.model.Transaction;
import com.beiji.transactionmanager.model.exception.TransactionErrorException;

import java.util.List;

/**
 * Interface for TransactionService
 */
public interface ITransactionService {

    /**
     * query all transactions
     *
     * @param page number of page
     * @param size size of page
     * @return a list of {@link Transaction} objects.
     */
    List<Transaction> queryAllTransactions(int page, int size);

    /**
     * Creates a new transaction
     *
     * @param transaction the {@link Transaction}
     * @return the created {@link Transaction}
     * @throws TransactionErrorException
     */
    Transaction createTransaction(Transaction transaction);

    /**
     * Updates a transaction
     *
     * @param transaction transaction
     * @return the updated {@link Transaction} object.
     * @throws TransactionErrorException
     */
    Transaction updateTransaction(Transaction transaction);

    /**
     * Deletes a transaction
     *
     * @param id the ID of the transaction to delete.
     * @throws TransactionErrorException
     */
    void deleteTransaction(String id);

    /**
     * get count of transactions
     *
     * @return count
     */
    int getTotalCount();

}

