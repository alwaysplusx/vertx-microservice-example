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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class MicroserviceVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(MicroserviceVerticle.class);
    protected ServiceDiscovery serviceDiscovery;
    protected CircuitBreaker circuitBreaker;
    private Set<Record> registeredRecords = new ConcurrentHashSet<>();

    @Override
    public final void start() throws Exception {
        init();

        doStart();
    }

    @Override
    public void stop() throws Exception {
        registeredRecords.forEach(e -> serviceDiscovery.unpublish(e.getRegistration(),
                ar -> {
                    if (ar.succeeded()) {
                        log.info("unpublish service {}", e);
                    } else {
                        log.error("unpublish service failed. {}", e, ar.cause());
                    }
                }));
    }

    private void init() throws Exception {
        config().getJsonObject("service-discovery");

        JsonObject defaultConfig = new JsonObject()
                .put("host", "localhost")
                .put("key", "vertx:records");

        this.serviceDiscovery = ServiceDiscovery
                .create(vertx,
                        new ServiceDiscoveryOptions()
                                .setBackendConfiguration(config().mergeIn(defaultConfig))
                );

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
        ).openHandler(e -> {
            //
        }).closeHandler(e -> {
            //
        }).halfOpenHandler(e -> {
            //
        }).retryPolicy(times -> times * 100l);

        // TODO create shutdown vertx http-endpoint, exception CONNECTION_CLOSED
        // vertx.createHttpServer()
        //        .requestHandler(ar -> vertx.close())
        //        .listen(9090);
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
