package com.example.order;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Log4jLogDelegateFactory;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.PrometheusScrapingHandler;
import io.vertx.micrometer.VertxPrometheusOptions;

public class OrderApplication {

    public static void main(String[] args) {

        System.setProperty("vertx.logger-delegate-factory-class-name", Log4jLogDelegateFactory.class.getName());

        MicrometerMetricsOptions micrometerMetricsOptions = new MicrometerMetricsOptions();

//        micrometerMetricsOptions.setInfluxDbOptions(
//                new VertxInfluxDbOptions()
//                        .setEnabled(true)
//                        .setDb("db0")
//                        .setUri("http://localhost:8060")
//                        .setUserName("admin")
//                        .setPassword("admin")
//        );


        VertxPrometheusOptions vpOptions = new VertxPrometheusOptions()
                .setEnabled(true);
//                .setStartEmbeddedServer(true)
//                .setEmbeddedServerOptions(new HttpServerOptions().setPort(8080))
//                .setEmbeddedServerEndpoint("/metrics/vertx");
        micrometerMetricsOptions.setPrometheusOptions(vpOptions).setEnabled(true);

        Vertx.clusteredVertx(new VertxOptions().setMetricsOptions(new MetricsOptions(micrometerMetricsOptions)), ar -> {
            Vertx vertx = ar.result();
            vertx.deployVerticle(OrderVerticle.class, new DeploymentOptions());

            Router router = Router.router(vertx);
            router.get("/mertics").handler(PrometheusScrapingHandler.create());
            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(8080);
        });
    }

}
