package com.mercadolibre.rampup_angelmarin.model.validator;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FashionCalculator {

    private String defaultCause;

    private String gridAttribute;

    private String rowAttribute;


    private String sizeAttr;


    private String filterSizeAttribute;

    private Map<String, Attribute> sizeAttributeBySite;

    private List<String> enabledDomains;

}
