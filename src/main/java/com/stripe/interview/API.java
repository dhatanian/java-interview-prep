package com.stripe.interview;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class API {

  private static final int MAX_EXECUTIONS = 5;
  private static final long WINDOW_SIZE_SECONDS = 2;
  private final DateTimeProvider dateTimeProvider;
  private Map<String, List<Instant>> lastExecutionsPerCustomer = new HashMap<>();

  public API(DateTimeProvider dateTimeProvider) {
    this.dateTimeProvider = dateTimeProvider;
  }

  public String myEndpoint(String customer) {
    List<Instant> lastExecutions = lastExecutionsPerCustomer
        .compute(customer, (k, v) -> v == null ? new ArrayList<>() : v);
    lastExecutions.add(dateTimeProvider.now());
    lastExecutions = clearOldExecutions(lastExecutions);

    if (lastExecutions.size() > MAX_EXECUTIONS) {
      throw new RateLimitedException();
    }

    return "Hello " + customer;
  }

  private List<Instant> clearOldExecutions(List<Instant> lastExecutions) {
    Instant rollingWindowStart = dateTimeProvider.now().minusSeconds(WINDOW_SIZE_SECONDS);
    return lastExecutions.stream()
        .filter(i -> i.isAfter(rollingWindowStart))
        .collect(Collectors.toList());
  }

}
