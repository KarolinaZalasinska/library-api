package mapper;

import dto.LibraryDto;
import model.Library;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibraryMapper extends EntityMapper<LibraryDto, Library> {
}
