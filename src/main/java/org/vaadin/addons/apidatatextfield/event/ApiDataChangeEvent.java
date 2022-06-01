package org.vaadin.addons.apidatatextfield.event;

import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;
import lombok.ToString;
import org.vaadin.addons.apidatatextfield.ApiDataTextField;

@Getter
@ToString
public class ApiDataChangeEvent extends ComponentEvent<ApiDataTextField<?>> {

    private final Object value;

    public ApiDataChangeEvent(ApiDataTextField<?> source, Object value, boolean fromClient) {
        super(source, fromClient);
        this.value = value;
    }
}
