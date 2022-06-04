package org.vaadin.addons.apidatatextfield.util;

public interface IdentifierConverter<ID> {
    String convertToString(ID identifier) throws Exception;

    ID convertFromString(String identifierString) throws Exception;
}
