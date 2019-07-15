package com.example.account.impl;

import com.example.account.Account;
import com.example.account.AccountService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class AccountServiceImpl implements AccountService {

    private final Vertx vertx;

    public AccountServiceImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public AccountService getAccountByName(String name, Handler<AsyncResult<Account>> handler) {
        // TODO read data from database
        Future<Account> future = Future.future();
        Account account = new Account();
        account.setId(1l);
        account.setEmail(name + "@domain.com");
        account.setName(name);
        account.setPhone("10000000000");
        future.complete(account);
        handler.handle(future);
        return this;
    }

}
