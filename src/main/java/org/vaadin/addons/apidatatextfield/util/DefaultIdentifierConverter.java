package org.vaadin.addons.apidatatextfield.util;

public class DefaultIdentifierConverter<ID> implements IdentifierConverter<ID> {

    private final Class<ID> identifierType;

    public DefaultIdentifierConverter(Class<ID> identifierType) {
        this.identifierType = identifierType;
    }

    @Override
    public String convertToString(ID identifier) {
        return identifier != null ? String.valueOf(identifier) : null;
    }

    @Override
    public ID convertFromString(String identifierString) {
        return (ID) ReflectionUtil.getValueFromString(identifierString, identifierType);
    }
}
