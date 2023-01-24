package com.mercadolibre.rampup_angelmarin.model.validator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    private String id;
    private String name;
}
