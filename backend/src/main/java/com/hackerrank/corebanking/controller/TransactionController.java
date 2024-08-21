package com.hackerrank.corebanking.controller;

import com.hackerrank.corebanking.model.Account;
import com.hackerrank.corebanking.model.Transaction;
import com.hackerrank.corebanking.repository.TransactionRepository;
import com.hackerrank.corebanking.service.AccountService;
import com.hackerrank.corebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/core-banking/transaction")
public class TransactionController {
  private final TransactionService transactionService;
  private final AccountService accountService;
  private final TransactionRepository transactionRepository;

  @Autowired
  public TransactionController(TransactionService transactionService, AccountService accountService, TransactionRepository transactionRepository) {
    this.transactionService = transactionService;
    this.accountService = accountService;
    this.transactionRepository = transactionRepository;
  }

  //create
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Transaction sendMoney(@RequestBody Transaction transaction) {
    Account toAccount = accountService.getAccountByAccountId(transaction.getToAccountId());
    if( toAccount != null) {
      return transactionService.sendMoney(transaction);
    }else {
      return null;
    }
  }

  //get
  @GetMapping("/transactionHistory")
  @ResponseStatus(HttpStatus.OK)
  public List<Transaction> transactionHistory(@RequestParam(name = "accountId") String accountId, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "fromDate") Date fromDate, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "toDate") Date toDate) {
    //this is need to support time stamp
    toDate.setHours(23);
    toDate.setMinutes(60);
    toDate.setSeconds(00);
    return transactionRepository.findTransactionsByDateCreatedBetweenAndFromAccountIdOrToAccountId(fromDate, toDate, accountId, accountId);
  }
}
