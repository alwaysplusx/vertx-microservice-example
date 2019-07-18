package com.example.account;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Log4jLogDelegateFactory;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountApplication {

    private static final Logger log = LoggerFactory.getLogger(AccountApplication.class);

    public static void main(String[] args) {
        // System.setProperty("vertx.cwd", "./target");
        System.setProperty("vertx.cacheDirBase", "./target");
        System.setProperty("vertx.logger-delegate-factory-class-name", Log4jLogDelegateFactory.class.getName());

        Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
                new MicrometerMetricsOptions()
                        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true)
                                .setStartEmbeddedServer(true)
                                .setEmbeddedServerOptions(new HttpServerOptions().setPort(8080))
                                .setEmbeddedServerEndpoint("/metrics/vertx"))
                        .setEnabled(true)));
        vertx.deployVerticle(AccountVerticle.class, new DeploymentOptions());

//        ConfigRetriever.create(Vertx.vertx(),
//                new ConfigRetrieverOptions()
//                        .addStore(new ConfigStoreOptions()
//                                .setType("file")
//                                .setFormat("json")
//                                .setConfig(new JsonObject().put("path", "application.json")))
//        ).listen(ar -> {
//            log.info("application.json changed, old: {}, new: {}", ar.getPreviousConfiguration(), ar.getNewConfiguration());
//        });
        // Vertx.vertx().deployVerticle(AccountVerticle.class, new DeploymentOptions());
    }

}
