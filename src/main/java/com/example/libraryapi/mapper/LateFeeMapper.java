package com.example.libraryapi.mapper;

import com.example.libraryapi.dto.LateFeeDto;
import com.example.libraryapi.model.LateFee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LateFeeMapper extends EntityMapper<LateFeeDto,LateFee>{
}
