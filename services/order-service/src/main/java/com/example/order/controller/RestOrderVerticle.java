package com.example.order.controller;

import com.example.account.AccountService;
import com.example.common.BaseVerticle;
import com.example.order.OrderService;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.types.EventBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestOrderVerticle extends BaseVerticle {

    private static final Logger log = LoggerFactory.getLogger(RestOrderVerticle.class);

    private final OrderService orderService;

    public RestOrderVerticle(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected void doStart() throws Exception {
        Router router = Router.router(vertx);
        router.get("/order/:id")
                .handler(this::handleGetOrderById);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(9091);
    }


    private void handleGetOrderById(RoutingContext routingContext) {

        EventBusService.getProxy(serviceDiscovery, AccountService.class, ar -> {
            if (ar.succeeded()) {
                AccountService proxyService = ar.result();
                proxyService.getAccountByName("foo", e -> {
                    log.info("get account from event bus proxy service {}, result is {}", proxyService, e.result());
                });
            } else {
                log.error("can't find event bus service proxy", ar.cause());
            }
        });

        routingContext.response().end("Hi " + routingContext.request().getParam("id"));
    }

}
