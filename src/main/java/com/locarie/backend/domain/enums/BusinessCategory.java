package com.locarie.backend.domain.enums;

import lombok.Getter;

@Getter
public enum BusinessCategory {
  FOOD("Food & Drink"),
  SHOP("Shop"),
  ART("Art"),
  LIFESTYLE("Lifestyle");

  private final String value;

  BusinessCategory(String value) {
    this.value = value;
  }
}
