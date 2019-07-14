package com.example.account.impl;

import com.example.account.Account;
import com.example.account.AccountService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class AccountServiceImpl implements AccountService {

    private final Vertx vertx;

    public AccountServiceImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public AccountService getAccountByName(String name, Handler<AsyncResult<Account>> handler) {
        return null;
    }

}
