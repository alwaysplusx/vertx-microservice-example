package com.example.order.impl;

import com.example.order.Order;
import com.example.order.OrderService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class OrderServiceImpl implements OrderService {

    private final Vertx vertx;

    public OrderServiceImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public OrderService getOrderById(Long id, Handler<AsyncResult<Order>> handler) {
        return null;
    }

}
