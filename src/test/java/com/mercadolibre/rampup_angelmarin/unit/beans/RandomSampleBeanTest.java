package com.mercadolibre.rampup_angelmarin.unit.beans;

import com.mercadolibre.rampup_angelmarin.beans.RandomSampleBean;
import com.mercadolibre.rampup_angelmarin.dtos.SampleDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomSampleBeanTest {

  @Test
  void randomPositiveTestOK() {
    RandomSampleBean randomSample = new RandomSampleBean();

    SampleDTO sample = randomSample.random();

    assertTrue(sample.getRandom() >= 0);
  }
}
