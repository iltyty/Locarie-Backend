package com.locarie.backend.mapper.impl.businesshours;

import com.locarie.backend.domain.dto.businesshours.BusinessHoursDto;
import com.locarie.backend.domain.entities.BusinessHoursEntity;
import com.locarie.backend.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BusinessHoursEntityDtoMapperImpl
    implements Mapper<BusinessHoursEntity, BusinessHoursDto> {
  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public BusinessHoursDto mapTo(BusinessHoursEntity businessHoursEntity) {
    return modelMapper.map(businessHoursEntity, BusinessHoursDto.class);
  }

  @Override
  public BusinessHoursEntity mapFrom(BusinessHoursDto businessHoursDto) {
    return modelMapper.map(businessHoursDto, BusinessHoursEntity.class);
  }
}
