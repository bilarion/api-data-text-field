package org.vaadin.addons.apidatatextfield.util;

public interface IdentifierConverter {
    String convertToString(Object identifier) throws Exception;

    Object convertFromString(String identifierString) throws Exception;
}
