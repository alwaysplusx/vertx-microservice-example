package com.example.account.controller;

import com.example.account.AccountService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestAccountVerticle extends AbstractVerticle {

    private final AccountService accountService;

    public RestAccountVerticle(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.get("/account/:name")
                .handler(this::handleGetAccountByName);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(9090);
    }

    private void handleGetAccountByName(RoutingContext routingContext) {
        String name = routingContext.request().getParam("name");
        accountService.getAccountByName(name, ar -> {
            routingContext.response().putHeader("content-type", "application/json");
            if (ar.succeeded()) {
                routingContext.response().end(JsonObject.mapFrom(ar.result()).toString());
            } else {
                routingContext.response().end("Error");
            }
        });
    }

}
