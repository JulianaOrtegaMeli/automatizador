package com.mercadolibre.rampup_angelmarin.metrics;


import java.util.HashMap;
import java.util.Map;

public class MetricData {

    private static final String STATUS = "status";

    private static final String EVENT = "event";

    private static final String STATUS_SUCCESS = "success";

    public static final String EVENT_CREATE_FRUIT = "save";

    public static Map<String, String> buildCreateFruitTags(String site, String eventType) {
        return new HashMap<String, String>() {{
            put(STATUS, STATUS_SUCCESS);
            put(EVENT, eventType);
            put("site_id", site);
        }};
    }


}
