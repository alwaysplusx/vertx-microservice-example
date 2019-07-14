package com.example.account;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.example.account.Account}.
 * NOTE: This class has been automatically generated from the {@link com.example.account.Account} original class using Vert.x codegen.
 */
public class AccountConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Account obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "email":
          if (member.getValue() instanceof String) {
            obj.setEmail((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof Number) {
            obj.setId(((Number)member.getValue()).longValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "phone":
          if (member.getValue() instanceof String) {
            obj.setPhone((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Account obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Account obj, java.util.Map<String, Object> json) {
    if (obj.getEmail() != null) {
      json.put("email", obj.getEmail());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getPhone() != null) {
      json.put("phone", obj.getPhone());
    }
  }
}
