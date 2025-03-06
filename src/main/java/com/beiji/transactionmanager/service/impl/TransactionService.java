package com.beiji.transactionmanager.service.impl;

import com.beiji.transactionmanager.model.Transaction;
import com.beiji.transactionmanager.model.exception.TransactionErrorException;
import com.beiji.transactionmanager.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransactionService implements ITransactionService {
    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public Transaction createTransaction(Transaction transaction) {
        if (transactions.containsKey(transaction.getId())) {
            throw new TransactionErrorException("交易ID " + transaction.getId() + " 已存在");
        }
        if (isIllegalAmount(transaction.getAmount())) {
            throw new TransactionErrorException("金额 " + transaction.getAmount() + " 非法");
        }
        transaction.setTimestamp(LocalDateTime.now());
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    private boolean isIllegalAmount(Double amount) {
        if (Objects.isNull(amount) || amount <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        if (!transactions.containsKey(transaction.getId())) {
            throw new TransactionErrorException("交易ID " + transaction.getId() + " 不存在");
        }
        transaction.setTimestamp(LocalDateTime.now());
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public void deleteTransaction(String id) {
        if (!transactions.containsKey(id)) {
            throw new TransactionErrorException("交易ID " + id + " 不存在");
        }
        transactions.remove(id);
    }

    public Transaction getById(String id) {
        Transaction transaction = transactions.get(id);
        if (transaction == null) {
            throw new TransactionErrorException("交易ID " + id + " 不存在");
        }
        return transaction;
    }

    public List<Transaction> queryAllTransactions(int page, int size) {
        List<Transaction> allTransactions = transactions.values().stream()
                .sorted(Comparator.comparing(Transaction::getId))
                .toList();
        int start = Math.min(page * size, allTransactions.size());
        int end = Math.min((page + 1) * size, allTransactions.size());
        return allTransactions.subList(start, end);
    }

    @Override
    public int getTotalCount() {
        return transactions.size();
    }

    // 添加 getter 方法以便测试访问
    public Map<String, Transaction> getTransactions() {
        return transactions;
    }
}