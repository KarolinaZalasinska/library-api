package mapper;

import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper<DTO, Entity> {
    Entity toEntity(DTO dto);
    DTO toDto(Entity entity);
    List<Entity> toEntityList(List<DTO> dtos);
    List<DTO> toDTOList(List<Entity> entities);

}
