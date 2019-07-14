package com.example.order;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


@VertxGen
@ProxyGen
public interface OrderService {

    String SERVICE_ADDRESS = "service.order";
    String SERVICE_NAME = "eb-order-service";

    @Fluent
    OrderService getOrderById(Long id, Handler<AsyncResult<Order>> handler);

}
