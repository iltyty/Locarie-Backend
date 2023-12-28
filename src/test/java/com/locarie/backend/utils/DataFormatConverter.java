package com.locarie.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.Collections;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class DataFormatConverter {
  public static MultiValueMap<String, String> objectToMultiValueMap(Object object)
      throws IllegalAccessException {
    if (object == null) {
      return null;
    }
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Class<?> currentClass = object.getClass();

    while (currentClass != null) {
      for (Field field : currentClass.getDeclaredFields()) {
        field.setAccessible(true);
        Object value = field.get(object);
        if (value != null) {
          map.put(field.getName(), Collections.singletonList(value.toString()));
        }
      }
      currentClass = currentClass.getSuperclass();
    }
    return map;
  }
}
