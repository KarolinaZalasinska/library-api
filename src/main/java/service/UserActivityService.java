package service;

import lombok.RequiredArgsConstructor;
import model.UserActivity;
import org.springframework.stereotype.Service;
import repository.UserActivityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    public final UserActivityRepository repository;
    public List<UserActivity> getUserBorrowHistory(final Long id){
        return repository.find(userId, "Borrow");
    }
}
