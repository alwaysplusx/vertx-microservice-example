package com.example.common;

import io.vertx.ext.web.Router;

/**
 * @author wuxii
 */
public abstract class RestMicroserviceVerticle extends MicroserviceVerticle {

    private Router router;

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

}
