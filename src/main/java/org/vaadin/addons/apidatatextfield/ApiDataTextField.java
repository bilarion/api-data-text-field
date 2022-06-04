package org.vaadin.addons.apidatatextfield;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import org.vaadin.addons.apidatatextfield.event.ApiDataChangeEvent;
import org.vaadin.addons.apidatatextfield.event.SimpleDataChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;


public class ApiDataTextField<T, ID> extends BaseApiDataTextField<T, ID> {

    public ApiDataTextField() {
        this(null, null);
    }

    public ApiDataTextField(String dataIdentifierField, Class<ID> identifierType) {
        this(dataIdentifierField, identifierType, null);
    }

    public ApiDataTextField(String dataIdentifierField, Class<ID> identifierType, Function<T, String> dataLabelGetter) {
        this(dataIdentifierField, identifierType, dataLabelGetter, null);
    }

    public ApiDataTextField(String dataIdentifierField, Class<ID> identifierType, Function<T, String> dataLabelGetter, Function<T, Component> componentProvider) {
        super();
        setDataIdentifierField(dataIdentifierField, identifierType);
        this.dataLabelGetter = Objects.requireNonNullElseGet(dataLabelGetter, () -> t -> Objects.toString(t, ""));
        if (componentProvider != null) {
            setRendererComponentProvider(componentProvider);
        }

        getContent().setItemLabelGenerator((ItemLabelGenerator<String>) s -> {
            ID id = stringToIdentifier(s);
            if (id != null && dataMap.get(id) != null) {
                return dataLabelGetter.apply(dataMap.get(id));
            }
            return s;
        });
        getContent().addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>) event -> {
            final String value = event.getValue();
            if (value != null) {
                final ID identifier = stringToIdentifier(value);
                if (identifier != null && dataMap.get(identifier) != null) {
                    T apiDataValue = dataMap.get(identifier);
                    fireEvent(new ApiDataChangeEvent(this, apiDataValue, false));
                }
            }

            fireEvent(new SimpleDataChangeEvent(this, value, false));
        });
    }

    public void setApiDataProvider(Function<String, List<T>> apiDataProvider) {
        if (dataIdentifierField == null) {
            throw new RuntimeException("Data identifier field is not set.");
        }

        getContent().setItems((CallbackDataProvider.FetchCallback<String, String>) query -> {
            query.getLimit();
            query.getOffset();
            query.getPage();

            String searchToken = null;
            if (query.getFilter().isPresent()) {
                searchToken = query.getFilter().get();
            }

            List<T> apiData;
            if (searchToken != null && searchToken.trim().length() > 0) {
                apiData = apiDataProvider.apply(searchToken);
            } else {
                apiData = new ArrayList<>();
            }

            if (apiData == null) {
                apiData = new ArrayList<>();
            }

            dataMap.clear();
            for (T data : apiData) {
                ID id = dataIdentifierGetter.apply(data);
                if (id != null) {
                    dataMap.put(id, data);
                }
            }
            return dataMap.keySet().stream().map(id -> id != null ? identifierToString(id) : null);
        });
    }

    public void setRendererComponentProvider(Function<T, Component> componentProducer) {
        if (dataIdentifierField == null) {
            throw new RuntimeException("Data identifier field is not set.");
        }

        getContent().setRenderer(new ComponentRenderer<>(value -> {
            T data = null;
            ID identifier = stringToIdentifier(value);
            if (identifier != null && dataMap.get(identifier) != null) {
                data = dataMap.get(identifier);
            }
            return componentProducer.apply(data);
        }));
    }

    public String getValue() {
        if (dataIdentifierField == null) {
            throw new RuntimeException("Data identifier field is not set.");
        }

        String value = getContent().getValue();
        if (value == null || value.trim().length() == 0 || dataMap == null || dataMap.isEmpty()) {
            return value;
        }

        ID identifier = stringToIdentifier(value);
        if (identifier != null && dataMap.get(identifier) != null) {
            return dataLabelGetter.apply(dataMap.get(identifier));
        }
        return value;
    }

    public T getApiDataValue() {
        if (dataIdentifierField == null) {
            throw new RuntimeException("Data identifier field is not set.");
        }

        String value = getContent().getValue();
        if (value == null || value.trim().length() == 0 || dataMap == null || dataMap.isEmpty()) {
            return null;
        }

        ID identifier = stringToIdentifier(value);
        if (identifier != null && dataMap.get(identifier) != null) {
            return dataMap.get(identifier);
        }
        return null;
    }

    public Registration addApiValueChangeListener(ComponentEventListener<ApiDataChangeEvent> listener) {
        return addListener(ApiDataChangeEvent.class, listener);
    }

    public Registration addSimpleValueChangeListener(ComponentEventListener<SimpleDataChangeEvent> listener) {
        return addListener(SimpleDataChangeEvent.class, listener);
    }
}
