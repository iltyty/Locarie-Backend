package com.locarie.backend.converters;

import com.locarie.backend.domain.enums.BusinessCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BusinessCategoryConverter implements AttributeConverter<BusinessCategory, String> {
  @Override
  public String convertToDatabaseColumn(BusinessCategory businessCategory) {
    return businessCategory.getValue();
  }

  @Override
  public BusinessCategory convertToEntityAttribute(String s) {
    return BusinessCategory.valueOf(s);
  }
}
