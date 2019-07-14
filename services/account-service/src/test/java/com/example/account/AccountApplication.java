package com.example.account;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Log4jLogDelegateFactory;

public class AccountApplication {

    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name", Log4jLogDelegateFactory.class.getName());
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(AccountVerticle.class, new DeploymentOptions());
    }

}
