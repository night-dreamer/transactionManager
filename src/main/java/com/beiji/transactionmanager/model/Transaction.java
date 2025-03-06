package com.beiji.transactionmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String id;
    private String accountId;        // 关键账号
    private String type;            // deposit/withdrawal
    private Double amount;
    private LocalDateTime timestamp;
    private String status;         // success/failure
    private String description;    // 可为空
}