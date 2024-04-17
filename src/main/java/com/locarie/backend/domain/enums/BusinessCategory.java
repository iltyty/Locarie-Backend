package com.locarie.backend.domain.enums;

import lombok.Getter;

@Getter
public enum BusinessCategory {
  COFFEE("Coffee"),
  RESTAURANT("Restaurant"),
  DRINKS("Drinks"),
  SHOP("Shop"),
  ART("Art"),
  LIFESTYLE("Lifestyle"),
  FASHION("Fashion"),
  FRESH_PRODUCE("Fresh Produce");

  private final String value;

  BusinessCategory(String value) {
    this.value = value;
  }
}
