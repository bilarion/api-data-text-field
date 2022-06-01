package org.vaadin.addons.apidatatextfield.demo;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.apidatatextfield.ApiDataTextField;

import java.util.Arrays;

@Route("")
public class AddonView extends Div {

    public AddonView() {
        ApiDataTextField<Test> theAddon = new ApiDataTextField<>(Test::getId, Long.class, Test::getName);
        theAddon.setApiDataProvider(s -> Arrays.asList(
                Test.builder().id(1L).name("Harry").description("Potter").build(),
                Test.builder().id(2L).name("Hermione").description("Granger").build(),
                Test.builder().id(3L).name("Draco").description("Malfoy").build(),
                Test.builder().id(4L).name("Lord").description("Voldemort").build()
        ));
        theAddon.setRendererComponentProvider(test -> {
            FlexLayout wrapper = new FlexLayout();
            wrapper.setAlignItems(FlexComponent.Alignment.CENTER);

            if (test == null) {
                return wrapper;
            }

            Div info = new Div();
            info.setText(test.getName());

            Div description = new Div();
            description.setText(String.valueOf(test.getDescription()));
            description.getStyle().set("font-size", "var(--lumo-font-size-s)");
            description.getStyle().set("color", "var(--lumo-secondary-text-color)");
            info.add(description);

            wrapper.add(info);
            return wrapper;
        });
        theAddon.setId("theAddon");
        add(theAddon);
    }
}
