package org.vaadin.addons.apidatatextfield.demo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Test {
    private Long id;
    private String name;
    private String description;
}
