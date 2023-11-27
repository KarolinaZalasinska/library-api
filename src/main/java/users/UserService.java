package users;

import com.example.libraryapi.model.Role;
import com.example.libraryapi.repository.RoleRepository;
import db.UserEntityRepository;
import domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.RegisterResponse;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserEntityRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public RegisterResponse register(String username, String password) {
        if (repository.findByUsernameIgnoreCase(username).isPresent()) {
            return RegisterResponse.failure("Account already exists.");
        }

        UserEntity entity = new UserEntity(username, encoder.encode(password));
        Role userRole = roleRepository.findByName("ROLE_USER");
        entity.setRoles(Set.of(userRole));

        repository.save(entity);
        return RegisterResponse.success();
    }


    public void registerUserWithRole(String username, String password, String roleName) {
        UserEntity userEntity = new UserEntity(username, password);
        Role role = new Role(roleName);

        userEntity.addRole(role);
        repository.save(userEntity);
    }
}
