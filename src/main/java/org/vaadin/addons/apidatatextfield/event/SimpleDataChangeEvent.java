package org.vaadin.addons.apidatatextfield.event;

import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;
import lombok.ToString;
import org.vaadin.addons.apidatatextfield.ApiDataTextField;

@Getter
@ToString
public class SimpleDataChangeEvent extends ComponentEvent<ApiDataTextField<?, ?>> {

    private final String value;

    public SimpleDataChangeEvent(ApiDataTextField<?, ?> source, String value, boolean fromClient) {
        super(source, fromClient);
        this.value = value;
    }
}