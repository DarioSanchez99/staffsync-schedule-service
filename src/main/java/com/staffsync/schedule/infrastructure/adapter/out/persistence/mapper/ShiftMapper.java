package com.staffsync.schedule.infrastructure.adapter.out.persistence.mapper;

import com.staffsync.schedule.domain.model.Shift;
import com.staffsync.schedule.infrastructure.adapter.out.persistence.entity.ShiftEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShiftMapper {

    ShiftEntity toEntity(Shift shift);

    Shift toDomain(ShiftEntity entity);
}
