package com.example.account.controller;

import brave.Tracing;
import brave.sampler.Sampler;
import brave.vertx.web.VertxWebTracing;
import com.example.account.AccountService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RestAccountVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(RestAccountVerticle.class);

    private final AccountService accountService;

    public RestAccountVerticle(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void start() throws Exception {

        Handler<RoutingContext> routingContextHandler = VertxWebTracing
                .create(buildTracing())
                .routingContextHandler();

        Router router = Router.router(vertx);
        router.route()
                .order(-1)
                .handler(routingContextHandler)
                .failureHandler(routingContextHandler);

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

    // TODO WEB-TRACING 异步改造, reporter升级为写队列?
    private Tracing buildTracing() throws UnknownHostException {
        return Tracing.newBuilder()
                .localIp(InetAddress.getLocalHost().getHostAddress())
                .localServiceName(AccountService.SERVICE_NAME)
                .sampler(Sampler.ALWAYS_SAMPLE)
                .spanReporter(e -> log.info("tracing info: {}", e))
                .build();
    }

}
