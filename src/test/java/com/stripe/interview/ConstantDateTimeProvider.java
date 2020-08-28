package com.stripe.interview;

import java.time.Instant;

public class ConstantDateTimeProvider implements DateTimeProvider {

  private Instant now;

  public ConstantDateTimeProvider(Instant now) {
    this.now = now;
  }

  @Override
  public Instant now() {
    return now;
  }

  public void setNow(Instant now) {
    this.now = now;
  }
}
