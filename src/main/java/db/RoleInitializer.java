package db;

import com.example.libraryapi.model.Role;
import com.example.libraryapi.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        Role roleUser = new Role("ROLE_USER");
        roleUser.setName("ROLE_USER");
        roleRepository.save(roleUser);

        Role roleAdmin = new Role("ROLE_USER");
        roleAdmin.setName("ROLE_ADMIN");
        roleRepository.save(roleAdmin);
    }
}
