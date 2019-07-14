package com.example.account;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
public interface AccountService {

    String SERVICE_ADDRESS = "service.account";

    String SERVICE_NAME = "eb-account-service";

    @Fluent
    AccountService getAccountByName(String name, Handler<AsyncResult<Account>> handler);

}
