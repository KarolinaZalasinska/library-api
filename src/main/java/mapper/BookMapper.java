package mapper;

import dto.BookDto;
import model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDto, Book> {
    Book toEntity(BookDto bookDto, @MappingTarget Book book);
    BookDto toDto(Book book);
}
