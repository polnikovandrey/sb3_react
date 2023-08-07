package com.mcfly.template.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class UserLoader implements CommandLineRunner {

    public static final int RANDOM_USERS_COUNT = 50;
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
//        final boolean emptyUsersTable = userRepository.count() == 0;
//        if (emptyUsersTable) {
//            log.info("Users table is empty. Populating the db with users.");
//            final Faker faker = new Faker();
//            final List<Role> roles = roleRepository.findAll();
//            final Role adminRole = roles.stream().filter(role -> RoleName.ROLE_ADMIN == role.getName()).findFirst().orElseThrow(IllegalStateException::new);
//            final Role userRole = roles.stream().filter(role -> RoleName.ROLE_USER == role.getName()).findFirst().orElseThrow(IllegalStateException::new);
//            log.info("Populating the db with admin user");
//            userRepository.save(
//                    User.builder()
//                            .username("admin")
//                            .email("admin@email.com")
//                            .password(passwordEncoder.encode("admin"))
//                            .firstName(faker.name().firstName())
//                            .lastName(faker.name().lastName())
//                            .middleName(faker.letterify("?", true))
//                            .roles(Set.of(adminRole))
//                            .build());
//            log.info("Populating the db with {} random users", RANDOM_USERS_COUNT);
//            final List<User> randomUsers
//                    = IntStream.range(2, 2 + RANDOM_USERS_COUNT)
//                    .mapToObj(i -> User.builder()
//                            .username(faker.letterify("user" + i))
//                            .email(faker.bothify("user" + i + "@gmail.com"))
//                            .password(passwordEncoder.encode("user" + i))
//                            .firstName(faker.name().firstName())
//                            .lastName(faker.name().lastName())
//                            .middleName(faker.letterify("?", true))
//                            .roles(Set.of(userRole))
//                            .build())
//                    .collect(Collectors.toList());
//            userRepository.saveAllAndFlush(randomUsers);
//            final long countUsers = userRepository.count();
//            log.info("{} users added", countUsers);
//        }
    }
}
