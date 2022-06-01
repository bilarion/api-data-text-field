package org.vaadin.addons.apidatatextfield.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ReflectionUtil {

    public static Object dereferenceValue(Object o, Class<?> c, String dotEncodedFieldName) {
        if (o == null) {
            return null;
        }

        try {
            String[] fieldTokens = dotEncodedFieldName.split("[.]");
            Class<?> currentC = c;
            Object currentO = o;
            Field result;
            for (String fieldToken : fieldTokens) {
                result = getAllDeclaredFields(currentC).get(fieldToken);
                currentC = result.getType();
                currentO = result.get(currentO);
                if (currentO == null) {
                    return null;
                }
            }
            return currentO;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static Map<String, Field> getAllDeclaredFields(Class<?> c) {
        Map<String, Field> result = new LinkedHashMap<>();

        Field[] fields = c.getDeclaredFields();

        if (c.getSuperclass() != null) {
            result.putAll(getAllDeclaredFields(c.getSuperclass()));
        }

        for (Field field : fields) {
            field.setAccessible(true);
            result.put(field.getName(), field);
        }
        return result;
    }

    public static Object getValueFromString(String value, Class<?> type) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        if (type == String.class) {
            return value;
        } else if (type == Byte.class || byte.class.isAssignableFrom(type)) {
            return Byte.valueOf(value);
        } else if (type == Short.class || short.class.isAssignableFrom(type)) {
            return Short.valueOf(value);
        } else if (type == Integer.class || int.class.isAssignableFrom(type)) {
            return Integer.valueOf(value);
        } else if (type == Long.class || long.class.isAssignableFrom(type)) {
            return Long.valueOf(value);
        } else if (type == Float.class || float.class.isAssignableFrom(type)) {
            return Float.valueOf(value);
        } else if (type == Double.class || double.class.isAssignableFrom(type)) {
            return Double.valueOf(value);
        } else if (type == BigInteger.class) {
            return new BigInteger(value);
        } else if (type == BigDecimal.class) {
            return new BigDecimal(value);
        } else {
            throw new RuntimeException("Unsupported data type!");
        }
    }
}
