package com.beiji.transactionmanager.controller;

import com.beiji.transactionmanager.common.SecurityLog;
import com.beiji.transactionmanager.model.Transaction;
import com.beiji.transactionmanager.model.exception.TransactionErrorException;
import com.beiji.transactionmanager.service.ITransactionService;
import com.beiji.transactionmanager.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TransactionWebController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/")
    @SecurityLog(level = "NORMAL")
    public String listTransactions(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("transactions", transactionService.queryAllTransactions(page, size));
        model.addAttribute("totalCount", transactionService.getTotalCount());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("transaction", new Transaction()); // 用于表单绑定
        return "index";
    }

    @PostMapping("/add")
    @SecurityLog(level = "MAJOR")
    public String addTransaction(@ModelAttribute Transaction transaction,
                                 Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        try {
            transactionService.createTransaction(transaction);
        } catch (TransactionErrorException e) {
            fillErrorMsg(model, page, size, e);
            return "index";
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    @SecurityLog(level = "CRITICAL")
    public String updateTransaction(@ModelAttribute Transaction transaction,
                                    Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        try {
            transactionService.updateTransaction(transaction);
        } catch (TransactionErrorException e) {
            fillErrorMsg(model, page, size, e);
            return "index";
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    @SecurityLog(level = "CRITICAL")
    public String deleteTransaction(@PathVariable String id,
                                    Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        try {
            transactionService.deleteTransaction(id);
        } catch (TransactionErrorException e) {
            fillErrorMsg(model, page, size, e);
            model.addAttribute("transaction", new Transaction());
            return "index";
        }
        return "redirect:/";
    }

    private void fillErrorMsg(Model model, int page, int size, TransactionErrorException e) {
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("transactions", transactionService.queryAllTransactions(page, size));
        model.addAttribute("totalCount", transactionService.getTotalCount());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
    }
}