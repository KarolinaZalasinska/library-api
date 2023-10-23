package mapper;

import dto.PublisherDto;
import model.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper extends EntityMapper<PublisherDto, Publisher> {
}
