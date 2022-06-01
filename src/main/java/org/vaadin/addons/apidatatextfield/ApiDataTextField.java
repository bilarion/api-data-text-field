package org.vaadin.addons.apidatatextfield;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import lombok.NonNull;
import lombok.Setter;
import org.vaadin.addons.apidatatextfield.util.DefaultIdentifierConverter;
import org.vaadin.addons.apidatatextfield.util.ReflectionUtil;
import org.vaadin.addons.apidatatextfield.event.ApiDataChangeEvent;
import org.vaadin.addons.apidatatextfield.event.SimpleDataChangeEvent;
import org.vaadin.addons.apidatatextfield.util.IdentifierConverter;

import java.util.*;
import java.util.function.Function;


public class ApiDataTextField<T> extends BaseApiDataTextField {

    @Setter
    private IdentifierConverter identifierConverter;

    private final Map<Object, T> dataMap = new LinkedHashMap<>();
    private final Function<T, Object> dataIdentifierGetter;
    private final Function<T, String> dataLabelGetter;

    public ApiDataTextField(@NonNull String dataIdentifierField,  Class<?> identifierType) {
        this(t -> {
            if (t == null) {
                return null;
            }
            return ReflectionUtil.dereferenceValue(t, identifierType, dataIdentifierField);
        }, identifierType);
    }

    public ApiDataTextField(@NonNull Function<T, Object> dataIdentifierGetter, Class<?> identifierType) {
        this(dataIdentifierGetter, identifierType, null);
    }

    public ApiDataTextField(@NonNull Function<T, Object> dataIdentifierGetter, Class<?> identifierType, Function<T, String> dataLabelGetter) {
        this(dataIdentifierGetter, identifierType, dataLabelGetter, null);
    }

    public ApiDataTextField(@NonNull Function<T, Object> dataIdentifierGetter, Class<?> identifierType, Function<T, String> dataLabelGetter, Function<T, Component> componentProvider) {
        super();
        this.dataIdentifierGetter = dataIdentifierGetter;
        this.dataLabelGetter = Objects.requireNonNullElseGet(dataLabelGetter, () -> t -> Objects.toString(t, ""));
        if (componentProvider != null) {
            setRendererComponentProvider(componentProvider);
        }
        this.identifierConverter = new DefaultIdentifierConverter(identifierType);

        getContent().setItemLabelGenerator((ItemLabelGenerator<String>) s -> {
            Object id = stringToIdentifier(s);
            if (id != null && dataMap.get(id) != null) {
                return dataLabelGetter.apply(dataMap.get(id));
            }
            return  s;
        });
        getContent().addValueChangeListener((ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>) event -> {
            final String value = event.getValue();
            if (value != null) {
                final Object identifier = stringToIdentifier(value);
                if (identifier != null && dataMap.get(identifier) != null) {
                    T apiDataValue = dataMap.get(identifier);
                    fireEvent(new ApiDataChangeEvent(this, apiDataValue, false));
                }
            }

            fireEvent(new SimpleDataChangeEvent(this, value, false));
        });
    }

    public void setApiDataProvider(Function<String, List<T>> apiDataProvider) {
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
                Object id = dataIdentifierGetter.apply(data);
                if (id != null) {
                    dataMap.put(id, data);
                }
            }
            return dataMap.keySet().stream().map(id -> id != null ? identifierToString(id) : null);
        });
    }

    public void setRendererComponentProvider(Function<T, Component> componentProducer) {
        getContent().setRenderer(new ComponentRenderer<>(value -> {
            T data = null;
            Object identifier = stringToIdentifier(value);
            if (identifier != null && dataMap.get(identifier) != null) {
                data = dataMap.get(identifier);
            }
            return componentProducer.apply(data);
        }));
    }

    public String getValue() {
        String value = getContent().getValue();
        if (value == null || value.trim().length() == 0 || dataMap == null || dataMap.isEmpty()) {
            return value;
        }

        Object identifier = stringToIdentifier(value);
        if (identifier != null && dataMap.get(identifier) != null) {
            return dataLabelGetter.apply(dataMap.get(identifier));
        }
        return value;
    }

    public T getApiDataValue() {
        String value = getContent().getValue();
        if (value == null || value.trim().length() == 0 || dataMap == null || dataMap.isEmpty()) {
            return null;
        }

        Object identifier = stringToIdentifier(value);
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

    private String identifierToString(Object identifier) {
        try {
            return identifierConverter.convertToString(identifier);
        } catch (Exception e) {
            return null;
        }
    }

    private Object stringToIdentifier(String identifierString) {
        try {
            return identifierConverter.convertFromString(identifierString);
        } catch (Exception e) {
            return null;
        }
    }
}
