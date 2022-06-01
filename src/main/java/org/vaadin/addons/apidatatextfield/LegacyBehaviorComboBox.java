package org.vaadin.addons.apidatatextfield;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;

import java.util.Collection;

@Tag("vcf-api-data-text-field")
@JsModule("./vcf-api-data-text-field.js")
@CssImport(value = "./vcf-api-data-text-field.css", themeFor = "vcf-api-data-text-field")
public class LegacyBehaviorComboBox<T> extends ComboBox<T> {

    public LegacyBehaviorComboBox(int pageSize) {
        super(pageSize);
    }

    public LegacyBehaviorComboBox() {
        super();
    }

    public LegacyBehaviorComboBox(String label) {
        super(label);
    }

    public LegacyBehaviorComboBox(String label, Collection<T> items) {
        super(label, items);
    }

    @SafeVarargs
    public LegacyBehaviorComboBox(String label, T... items) {
        super(label, items);
    }
}
