package com.stripe.interview;

import java.time.Clock;
import java.time.Instant;

public class ClockDateTimeProvider implements DateTimeProvider {

  private Clock clock = Clock.systemUTC();

  public Instant now() {
    return clock.instant();
  }

}
