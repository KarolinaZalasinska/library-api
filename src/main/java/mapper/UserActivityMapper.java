package mapper;

import dto.UserActivityDto;
import model.UserActivity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserActivityMapper extends EntityMapper<UserActivityDto, UserActivity>{
}
