package com.stripe.interview;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import org.junit.Test;

public class APITest {

  private static final String CUSTOMER = "my_customer";
  private static final String CUSTOMER_2 = "my_second_customer";

  private ConstantDateTimeProvider dateTimeProvider = new ConstantDateTimeProvider(Instant.EPOCH);
  private API underTest = new API(dateTimeProvider);

  @Test
  public void shouldReturnResults() {
    String result = underTest.myEndpoint(CUSTOMER);

    assertEquals("Hello my_customer", result);
  }

  @Test(expected = RateLimitedException.class)
  public void shouldFailWith6CallsWithin2Seconds() {
    for (int i = 0; i < 6; i++) {
      underTest.myEndpoint(CUSTOMER);
    }
  }

  @Test
  public void shouldPassWith6CallsOverMoreThan2Seconds() {
    for (int i = 0; i < 3; i++) {
      underTest.myEndpoint(CUSTOMER);
    }

    dateTimeProvider.setNow(dateTimeProvider.now().plusSeconds(3));

    for (int i = 0; i < 3; i++) {
      underTest.myEndpoint(CUSTOMER);
    }
  }

  @Test
  public void shouldPassAfterFailingRateLimitPreviously() {
    for (int i = 0; i < 5; i++) {
      underTest.myEndpoint(CUSTOMER);
    }

    dateTimeProvider.setNow(dateTimeProvider.now().plusSeconds(1));

    try {
      for (int i = 0; i < 5; i++) {
        underTest.myEndpoint(CUSTOMER);
      }
    } catch (RateLimitedException e) {
    }

    dateTimeProvider.setNow(dateTimeProvider.now().plusSeconds(1));

    underTest.myEndpoint(CUSTOMER);
  }

  @Test
  public void shouldFailForNoisyCustomerButNotForOthers() {
    for (int i = 0; i < 5; i++) {
      underTest.myEndpoint(CUSTOMER);
    }

    try {
      underTest.myEndpoint(CUSTOMER);
    } catch (RateLimitedException e) {
    }

    underTest.myEndpoint(CUSTOMER_2);
  }
}