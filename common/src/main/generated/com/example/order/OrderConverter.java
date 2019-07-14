package com.example.order;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.example.order.Order}.
 * NOTE: This class has been automatically generated from the {@link com.example.order.Order} original class using Vert.x codegen.
 */
public class OrderConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Order obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "id":
          if (member.getValue() instanceof Number) {
            obj.setId(((Number)member.getValue()).longValue());
          }
          break;
        case "serialNumber":
          if (member.getValue() instanceof String) {
            obj.setSerialNumber((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Order obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Order obj, java.util.Map<String, Object> json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getSerialNumber() != null) {
      json.put("serialNumber", obj.getSerialNumber());
    }
  }
}
