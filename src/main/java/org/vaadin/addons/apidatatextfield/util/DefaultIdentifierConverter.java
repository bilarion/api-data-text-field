package org.vaadin.addons.apidatatextfield.util;

public class DefaultIdentifierConverter implements IdentifierConverter {

    private final Class<?> identifierType;

    public DefaultIdentifierConverter(Class<?> identifierType) {
        this.identifierType = identifierType;
    }

    @Override
    public String convertToString(Object identifier) {
        return identifier != null ? String.valueOf(identifier) : null;
    }

    @Override
    public Object convertFromString(String identifierString) {
        return ReflectionUtil.getValueFromString(identifierString, identifierType);
    }
}
