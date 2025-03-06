package com.beiji.transactionmanager;

import com.beiji.transactionmanager.model.Transaction;
import com.beiji.transactionmanager.model.exception.TransactionErrorException;
import com.beiji.transactionmanager.service.impl.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        // 在每个测试用例前清除数据
        transactionService.getTransactions().clear();
    }

    @Test
    public void testCreateTransactionTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setAccountId("acc1");
        transaction.setType("deposit");
        transaction.setAmount(100.0);
        transaction.setStatus("success");

        Transaction created = transactionService.createTransaction(transaction);
        assertNotNull(created.getTimestamp());
        assertEquals("1", created.getId());
        assertEquals(1, transactionService.getTotalCount());
    }

    @Test
    public void testCreateTransactionDuplicateTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setAccountId("acc1");
        transaction.setType("deposit");
        transaction.setAmount(100.0);
        transaction.setStatus("success");

        transactionService.createTransaction(transaction);
        Exception exception = assertThrows(TransactionErrorException.class, () ->
                transactionService.createTransaction(transaction));
        assertEquals("交易ID 1 已存在", exception.getMessage());
    }

    @Test
    public void testUpdateTransactionTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setAccountId("acc1");
        transaction.setType("deposit");
        transaction.setAmount(100.0);
        transaction.setStatus("success");

        transactionService.createTransaction(transaction);
        transaction.setAmount(200.0);
        Transaction updated = transactionService.updateTransaction(transaction);
        assertEquals(200.0, updated.getAmount());
        assertEquals(1, transactionService.getTotalCount());
    }

    @Test
    public void testUpdateTransactionNonExistentTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId("nonexistent");
        transaction.setAccountId("acc1");
        transaction.setType("deposit");
        transaction.setAmount(100.0);
        transaction.setStatus("success");

        Exception exception = assertThrows(TransactionErrorException.class, () ->
                transactionService.updateTransaction(transaction));
        assertEquals("交易ID nonexistent 不存在", exception.getMessage());
    }

    @Test
    public void testDeleteTransactionTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setAccountId("acc1");
        transaction.setType("deposit");
        transaction.setAmount(100.0);
        transaction.setStatus("success");

        transactionService.createTransaction(transaction);
        transactionService.deleteTransaction("1");
        assertEquals(0, transactionService.getTotalCount());
    }

    @Test
    public void testDeleteTransactionNonExistentTransaction() {
        Exception exception = assertThrows(TransactionErrorException.class, () ->
                transactionService.deleteTransaction("nonexistent"));
        assertEquals("交易ID nonexistent 不存在", exception.getMessage());
    }

    @Test
    public void testGetById() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setAccountId("acc1");
        transaction.setType("deposit");
        transaction.setAmount(100.0);
        transaction.setStatus("success");

        transactionService.createTransaction(transaction);
        Transaction retrieved = transactionService.getById("1");
        assertEquals("1", retrieved.getId());
        assertEquals("acc1", retrieved.getAccountId());
    }

    @Test
    public void testGetNonExistentTransaction() {
        Exception exception = assertThrows(TransactionErrorException.class, () ->
                transactionService.getById("nonexistent"));
        assertEquals("交易ID nonexistent 不存在", exception.getMessage());
    }

    @Test
    public void testQueryAllTransactionsWithPagination() {
        // 创建多个交易
        for (int i = 1; i <= 15; i++) {
            Transaction t = new Transaction();
            t.setId(String.valueOf(i));
            t.setAccountId("acc" + i);
            t.setType(i % 2 == 0 ? "deposit" : "withdrawal");
            t.setAmount(i * 10.0);
            t.setStatus(i % 2 == 0 ? "success" : "failure");
            transactionService.createTransaction(t);
        }

        assertEquals(10, transactionService.queryAllTransactions(0, 10).size()); // 第一页
        assertEquals(5, transactionService.queryAllTransactions(1, 10).size());  // 第二页
        assertEquals(0, transactionService.queryAllTransactions(2, 10).size());  // 第三页
    }

    @Test
    public void testQueryAllTransactionsWithEmptyData() {
        assertEquals(0, transactionService.queryAllTransactions(0, 10).size());
    }
}