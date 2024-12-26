package com.dailywork.dreamshops.data;


import com.dailywork.dreamshops.model.Role;
import com.dailywork.dreamshops.model.User;
import com.dailywork.dreamshops.repository.RoleRepository;
import com.dailywork.dreamshops.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        createDefaultRolesIfNotExists(defaultRoles);
        createDefaultUsersIfNotExists();
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUsersIfNotExists(){
        Role userRole = roleRepository.findByName("ROLE_USER").get(0);
        for(int i = 1; i <= 5; i++ ){
            String defaultEmail = "user" + i + "@dreamshops.com";
            if(!userRepository.existsByEmail(defaultEmail)){
                User user = new User();
                user.setFirstName("User");
                user.setLastName(String.valueOf(i));
                user.setEmail(defaultEmail);
                user.setRoles(Set.of(userRole));
                user.setPassword(passwordEncoder.encode("password"));
                userRepository.save(user);
                System.out.println("Default user created with email: " + defaultEmail);
            }
        }
    }

    private void createDefaultAdminIfNotExists(){
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get(0);
        for(int i = 1; i <= 2; i++ ){
            String defaultEmail = "admin" + i + "@dreamshops.com";
            if(!userRepository.existsByEmail(defaultEmail)){
                User user = new User();
                user.setFirstName("Admin");
                user.setLastName(String.valueOf(i));
                user.setEmail(defaultEmail);
                user.setRoles(Set.of(adminRole));
                user.setPassword(passwordEncoder.encode("password"));
                userRepository.save(user);
                System.out.println("Default admin created with email: " + defaultEmail);
            }
        }
    }

    private void createDefaultRolesIfNotExists(Set<String> roles){
       roles.stream().
               filter(role-> roleRepository.findByName(role).isEmpty())
               .map(Role :: new)
               .forEach(roleRepository::save);
    }
}
