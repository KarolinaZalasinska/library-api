package service;

import dto.UserActivityDto;
import lombok.RequiredArgsConstructor;
import mapper.UserActivityMapper;
import model.UserActivity;
import org.springframework.stereotype.Service;
import repository.UserActivityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    public final UserActivityRepository repository;
    public final UserActivityMapper mapper;

    public List<UserActivityDto> getUserBorrowHistory(final Long id) {
        List<UserActivity> userActivities = repository.findByUserIdAndActionBorrow(id, "Borrow");
        return mapper.toDTOList(userActivities);
    }

    public List<UserActivityDto> getUserReturnHistory(final Long id) {
        List<UserActivity> userActivities = repository.findByUserIdAndActionReturn(id, "Return");
        return mapper.toDTOList(userActivities);
    }
}
