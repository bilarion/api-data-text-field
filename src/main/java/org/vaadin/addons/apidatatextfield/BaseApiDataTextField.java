package org.vaadin.addons.apidatatextfield;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.dataview.ComboBoxDataView;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;

public abstract class BaseApiDataTextField extends Composite<LegacyBehaviorComboBox<String>> implements HasLabel, HasValue<HasValue.ValueChangeEvent<String>, String> {

    public BaseApiDataTextField() {
        super();
        ComboBox<String> cb = getContent();
        cb.setAllowCustomValue(true);
        cb.addCustomValueSetListener(event -> cb.setValue(event.getDetail()));
        cb.setItems(new ArrayList<>());
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

    public void setErrorMessage(String errorMessage) {
        getContent().setErrorMessage(errorMessage);
    }

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

    public void setInvalid(boolean invalid) {
        getContent().setInvalid(invalid);
    }

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
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<String>> valueChangeListener) {
        return getContent().addValueChangeListener(valueChangeListener);
    }
}
