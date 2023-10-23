package mapper;

import dto.CopyDto;
import model.Copy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CopyMapper extends EntityMapper<CopyDto, Copy> {
}
