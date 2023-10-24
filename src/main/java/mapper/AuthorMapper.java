package mapper;

import dto.AuthorDto;
import model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDto, Author> {
    // Implementacja mapowania AuthorDto na Author
    Author toEntity(AuthorDto authorDto, @MappingTarget Author author);

    // Implementacja mapowania Author na AuthorDto
    AuthorDto toDto(Author author);
}