package org.vaadin.addons.apidatatextfield;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.dataview.ComboBoxDataView;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.addons.apidatatextfield.util.DefaultIdentifierConverter;
import org.vaadin.addons.apidatatextfield.util.IdentifierConverter;
import org.vaadin.addons.apidatatextfield.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class BaseApiDataTextField<T, ID> extends Composite<LegacyBehaviorComboBox<String>> implements HasSize, HasValidation, HasHelper, HasTheme, HasLabel, HasStyle, HasText, HasValue<HasValue.ValueChangeEvent<String>, String> {

    @Setter
    @Getter
    protected IdentifierConverter<ID> identifierConverter;

    protected final Map<ID, T> dataMap = new LinkedHashMap<>();

    @Getter
    protected String dataIdentifierField;

    protected Function<T, ID> dataIdentifierGetter;
    @Setter
    @Getter
    protected Function<T, String> dataLabelGetter;
    @Setter
    @Getter
    protected Class<ID> identifierType;

    public BaseApiDataTextField() {
        super();
        ComboBox<String> cb = getContent();
        cb.setAllowCustomValue(true);
        cb.addCustomValueSetListener(event -> cb.setValue(event.getDetail()));
        cb.setItems(new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public void setDataIdentifierField(String dataIdentifierField, Class<ID> identifierType) {
        dataIdentifierGetter = null;
        this.dataIdentifierField = dataIdentifierField;
        if (dataIdentifierField != null && identifierType != null) {
            dataIdentifierGetter = t -> {
                if (t == null) {
                    return null;
                }
                return (ID) ReflectionUtil.dereferenceValue(t, t.getClass(), dataIdentifierField);
            };
            this.identifierType = identifierType;
            if (this.identifierConverter == null || this.identifierConverter instanceof DefaultIdentifierConverter) {
                this.identifierConverter = new DefaultIdentifierConverter<>(identifierType);
            }
        }
    }

    protected String identifierToString(ID identifier) {
        try {
            return identifierConverter.convertToString(identifier);
        } catch (Exception e) {
            return null;
        }
    }

    protected ID stringToIdentifier(String identifierString) {
        try {
            return identifierConverter.convertFromString(identifierString);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        getContent().setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return getContent().isVisible();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        getContent().setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return getContent().isReadOnly();
    }

    @Override
    public void setValue(String value) {
        getContent().setValue(value);
    }

    @Override
    public void setLabel(String label) {
        getContent().setLabel(label);
    }

    @Override
    public String getLabel() {
        return getContent().getLabel();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        getContent().setRequiredIndicatorVisible(requiredIndicatorVisible);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return getContent().isRequiredIndicatorVisible();
    }

    public void setOpened(boolean opened) {
        getContent().setOpened(opened);
    }

    public boolean isOpened() {
        return getContent().isOpened();
    }

    public void setEnabled(boolean enabled) {
        getContent().setEnabled(enabled);
    }

    public boolean isEnabled() {
        return getContent().isEnabled();
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        getContent().setErrorMessage(errorMessage);
    }

    @Override
    public String getErrorMessage() {
        return getContent().getErrorMessage();
    }

    public void setPlaceholder(String placeholder) {
        getContent().setPlaceholder(placeholder);
    }

    public String getPlaceholder() {
        return getContent().getPlaceholder();
    }

    public void setClearButtonVisible(boolean clearButtonVisible) {
        getContent().setClearButtonVisible(clearButtonVisible);
    }

    public boolean isClearButtonVisible() {
        return getContent().isClearButtonVisible();
    }

    public void setPreventInvalidInput(boolean preventInvalidInput) {
        getContent().setPreventInvalidInput(preventInvalidInput);
    }

    public boolean isPreventInvalidInput() {
        return getContent().isPreventInvalidInput();
    }

    @Override
    public void setInvalid(boolean invalid) {
        getContent().setInvalid(invalid);
    }

    @Override
    public boolean isInvalid() {
        return getContent().isInvalid();
    }

    public ComboBoxDataView<String> getGenericDataView() {
        return getContent().getGenericDataView();
    }

    @Override
    public String getEmptyValue() {
        return getContent().getEmptyValue();
    }

    public void blur() {
        getContent().blur();
    }

    @Override
    public void clear() {
        getContent().clear();
    }

    public void focus() {
        getContent().focus();
    }

    @Override
    public Registration addValueChangeListener(HasValue.ValueChangeListener<? super HasValue.ValueChangeEvent<String>> valueChangeListener) {
        return getContent().addValueChangeListener(valueChangeListener);
    }
}
