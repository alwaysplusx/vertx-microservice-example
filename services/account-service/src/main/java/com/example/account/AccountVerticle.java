package com.example.account;

import com.example.account.controller.RestAccountVerticle;
import com.example.account.impl.AccountServiceImpl;
import com.example.common.BaseVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.EventBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountVerticle extends BaseVerticle {

    private AccountService accountService;

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Override
    protected void doStart() throws Exception {
        log.info("start account verticle");
        this.accountService = new AccountServiceImpl(vertx);
        // register proxy on event bus
        registerProxy(AccountService.SERVICE_ADDRESS, AccountService.class, this.accountService);
        // publish to service discovery
        Record record = EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class);
        publish(record)
                .setHandler(ar -> deployRestVerticle());
    }

    private void deployRestVerticle() {
        vertx.deployVerticle(
                new RestAccountVerticle(accountService),
                new DeploymentOptions().setConfig(config())
        );
    }

}
