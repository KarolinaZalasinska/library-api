package mapper;

import dto.UserActivityDto;
import model.Borrow;
import model.UserActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserActivityMapper extends EntityMapper<UserActivityDto, UserActivity> {
    UserActivityDto mapBorrowToUserActivityDto(Borrow borrow);
}
