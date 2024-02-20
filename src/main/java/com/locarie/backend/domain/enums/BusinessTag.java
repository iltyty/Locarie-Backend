package com.locarie.backend.domain.enums;

public enum BusinessTag {
  FOOD("Food & Drink"),
  SHOP("Shop"),
  ART("Art"),
  LIFESTYLE("Lifestyle");
  private final String value;

  BusinessTag(String value) {
    this.value = value;
  }
}
