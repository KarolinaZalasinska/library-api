package mapper;

import dto.LateFeeDto;
import model.LateFee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LateFeeMapper extends EntityMapper<LateFeeDto,LateFee>{
}
