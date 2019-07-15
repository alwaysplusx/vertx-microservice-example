package com.example.order;

import com.example.common.MicroserviceVerticle;
import com.example.order.controller.RestOrderVerticle;
import com.example.order.impl.OrderServiceImpl;
import io.vertx.core.DeploymentOptions;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.EventBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderVerticle extends MicroserviceVerticle {

    private static final Logger log = LoggerFactory.getLogger(OrderVerticle.class);

    private OrderService orderService;

    @Override
    protected void doStart() throws Exception {
        log.info("start order verticle");
        this.orderService = new OrderServiceImpl(vertx);

        registerProxy(OrderService.SERVICE_ADDRESS, OrderService.class, this.orderService);

        Record record = EventBusService.createRecord(OrderService.SERVICE_NAME, OrderService.SERVICE_ADDRESS, OrderService.class);
        publish(record)
                .setHandler(ar -> deployRestVerticle());
    }

    private void deployRestVerticle() {
        vertx.deployVerticle(
                new RestOrderVerticle(orderService),
                new DeploymentOptions().setConfig(config())
        );
    }

}
