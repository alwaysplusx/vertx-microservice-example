package com.example.common;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.serviceproxy.ServiceBinder;

import java.util.Set;

public abstract class BaseVerticle extends AbstractVerticle {

    protected ServiceDiscovery serviceDiscovery;
    private CircuitBreaker circuitBreaker;
    protected Set<Record> registeredRecords = new ConcurrentHashSet<>();

    @Override
    public final void start() throws Exception {
        init();

        doStart();
    }

    private void init() throws Exception {
        this.serviceDiscovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));
//                .registerServiceImporter(
//                        new ConsulServiceImporter(),
//                        new JsonObject().put("host", "localhost").put("port", 8500).put("scan-period", 2000)
//                );

        JsonObject cbOptions = config().getJsonObject("circuit-breaker") != null
                ? config().getJsonObject("circuit-breaker")
                : new JsonObject();
        this.circuitBreaker = CircuitBreaker.create(cbOptions.getString("name", "circuit-breaker"),
                vertx,
                new CircuitBreakerOptions()
                        .setMaxFailures(cbOptions.getInteger("max-failures", 5))
                        .setTimeout(cbOptions.getLong("timeout", 10000L))
                        .setFallbackOnFailure(true)
                        .setResetTimeout(cbOptions.getLong("reset-timeout", 30000L))
        );
    }

    protected void doStart() throws Exception {

    }

    protected <T> void registerProxy(String address, Class<T> clazz, T service) {
        new ServiceBinder(vertx)
                .setAddress(address)
                .register(clazz, service);
    }

    // publish to service discovery
    protected Future<Void> publish(Record record) {
        if (serviceDiscovery == null) {
            try {
                init();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        Future<Void> future = Future.future();
        serviceDiscovery.publish(record, ar -> {
            if (ar.succeeded()) {
                registeredRecords.add(record);
                future.complete();
            } else {
                future.fail(ar.cause());
            }
        });
        return future;
    }

}
