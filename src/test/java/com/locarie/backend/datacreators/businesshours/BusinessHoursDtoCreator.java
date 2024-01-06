package com.locarie.backend.datacreators.businesshours;

import com.locarie.backend.domain.dto.businesshours.BusinessHoursDto;
import com.locarie.backend.domain.entities.BusinessHoursEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.businesshours.BusinessHoursEntityDtoMapperImpl;
import java.util.List;

public class BusinessHoursDtoCreator {
  private static final Mapper<BusinessHoursEntity, BusinessHoursDto> businessHoursMapper =
      new BusinessHoursEntityDtoMapperImpl();

  public static List<BusinessHoursDto> businessHoursDtos() {
    return BusinessHoursEntityCreator.businessHoursEntities().stream()
        .map(businessHoursMapper::mapTo)
        .toList();
  }

  public static List<BusinessHoursDto> randomBusinessHoursDtos() {
    return BusinessHoursEntityCreator.randomBusinessHoursEntities().stream()
        .map(businessHoursMapper::mapTo)
        .toList();
  }
}
