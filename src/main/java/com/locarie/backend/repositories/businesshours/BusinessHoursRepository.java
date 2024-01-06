package com.locarie.backend.repositories.businesshours;

import com.locarie.backend.domain.entities.BusinessHoursEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessHoursRepository extends CrudRepository<BusinessHoursEntity, Long> {}
