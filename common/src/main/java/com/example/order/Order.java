package com.example.order;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Date;

@DataObject(generateConverter = true)
public class Order {

    private Long id;
    private String serialNumber;
    private Date createdAt;

    public Order() {
    }

    public Order(JsonObject json) {
        OrderConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        OrderConverter.toJson(this, json);
        return json;
    }

    public Long getId() {
        return id;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Order setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Order setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
