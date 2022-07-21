package com.mercadolibre.rampup_angelmarin.metrics;

public enum DatadogMetricName {

    CREATE_FRUIT("rampup.onboarding.fruit");

    private String name;

    public String getName() { return this.name; }

    DatadogMetricName(String name) { this.name = name; }
}
