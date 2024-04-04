package net.smc.services;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.smc.repositories.UserRepository;
import net.smc.dto.UserDto;
import net.smc.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> {
            userDtos.add(new UserDto(user));
        });
        return userDtos;
    }

    public String getUserRole(String login) {
        User user = userRepository.findByLogin(login);
        return user.getRole();
    }

    public UserDto updateUser(UserDto userDto) {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("admin")) {
            throw new RuntimeException("Необходима роль администратора!");
        }
        User user = userRepository.findByLogin(userDto.login);
        if (user == null) {
            throw new RuntimeException("Не найден пользователь");
        }
        user.update(userDto);
        if (!isValidatedOfChiefCreate(user)) {
            throw new RuntimeException("Нельзя создавать второго шефа!");
        }
        userRepository.save(user);
        return userDto;
    }

    public void deleteUser(String login) {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("admin")) {
            throw new RuntimeException("Необходима роль администратора!");
        }
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new RuntimeException("Не найден пользователь");
        }
        userRepository.delete(user);
    }

    public boolean isValidatedOfChiefCreate(User userForCreateOrUpdate) {
        Boolean validated = false;
        User chief = userRepository.findByRole("chief");
        //если есть шеф, и новый пользователь не совпадает с ним по логину, запрещаем
        if (chief != null && !chief.getLogin().equals(userForCreateOrUpdate.getLogin())
                && userForCreateOrUpdate.getRole().equals("chief")) {
            throw new RuntimeException("Нельзя создать нового начальника!");
        }
        validated = true;
        return validated;
    }


    public boolean isValidatedOfDublicateCreate(User userForCreate) {
        Boolean validated = false;
        User dublicate = userRepository.findByLogin(userForCreate.login);
        //если есть дубликат, запрещаем
        if (dublicate != null) {
            throw new RuntimeException("Логин не уникален!");
        }
        validated = true;
        return validated;
    }

    public UserDto getUser(String login) {
        User user = userRepository.findByLogin(login);
        String userRoles = user.getRole();
        return new UserDto(user);
    }

    public void userDataChange(UserDto userWithChanges) {
        User user = this.userRepository.findByLogin(userWithChanges.getLogin());
        user.setFullName(userWithChanges.getFullName());
        user.setEmail(userWithChanges.getEmail());
        userRepository.saveAndFlush(user);
    }

    public Boolean passwordChange(String login, String oldPassword, String newPassword) {
        User user = this.userRepository.findByLogin(login);
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.saveAndFlush(user);
            return true;
        } else {
            return false;
        }

    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(auth.getName());
    }

    public List<String> getEmailsByRole(String role) {
        List<User> users = userRepository.findAll().stream().filter(e -> e.getRole().equals(role)).toList();
        if (users == null || users.isEmpty()) {
            new RuntimeException("Не найдены пользователи по роли");
        }
        return users.stream().map(User::getEmail).toList();
    }

    public String getEmailByLogin(String login) {
        //user обязательно один
        List<User> users = userRepository.findAll().stream().filter(e -> e.getRole().equals(login)).toList();
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("Не найден пользователь по логину");
        }
        return users.get(0).getEmail();
    }


    //////////////init admin////////////////////
    @Value("${default-user.login}")
    private String adminLogin;
    @Value("${default-user.password}")
    private String adminPassword;
    @Value("${default-user.email}")
    private String adminEmail;
    @Value("${default-user.full-name}")
    private String adminFullName;

    @Value("${initialize.admin}")
    private boolean adminInitialization;

    @Value("${initialize.user}")
    private boolean userInitialization;

    @PostConstruct
    public void initializeAllUsers() {
        if (adminInitialization) {
            initializeAdmin();
        }
        if (userInitialization) {
            initializeAllOtherUsers();
        }
    }

    public void initializeAdmin() {
        User adminRole = userRepository.findByLogin("admin");
        if (adminRole == null) {
            this.generateAdminUser();
        }
    }

    public void initializeAllOtherUsers() {
        User userRole = userRepository.findByLogin("user");
        if (userRole == null) generateUserUser();
        User chiefRole = userRepository.findByLogin("chief");
        if (chiefRole == null) generateChiefUser();
        User contragentRole = userRepository.findByLogin("contragent");
        if (contragentRole == null) generateContragentUser();
    }

    private void generateAdminUser() {
        User admin = new User();
        admin.setLogin(adminLogin);
        admin.setPassword(adminPassword);
        admin.setEmail(adminEmail);
        admin.setFullName(adminFullName);
        User user = User.builder()
                .login(admin.getLogin())
                .password(passwordEncoder.encode(admin.getPassword()))
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role("admin")
                .build();
        userRepository.save(user);
    }

    private void generateUserUser() {
        User admin = new User();
        admin.setLogin("user");
        admin.setPassword("user");
        admin.setEmail("user-42-user@mail.com");
        admin.setFullName("Юзер Юзерович Юзеров");
        User user = User.builder()
                .login(admin.getLogin())
                .password(passwordEncoder.encode(admin.getPassword()))
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role("user")
                .build();
        userRepository.save(user);
    }

    private void generateChiefUser() {
        User admin = new User();
        admin.setLogin("chief");
        admin.setPassword("chief");
        admin.setEmail("chief-42-chief@mail.com");
        admin.setFullName("Шеф Шефович Шефов");
        User user = User.builder()
                .login(admin.getLogin())
                .password(passwordEncoder.encode(admin.getPassword()))
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role("chief")
                .build();
        userRepository.save(user);
    }

    private void generateContragentUser() {
        User admin = new User();
        admin.setLogin("contragent");
        admin.setPassword("contragent");
        admin.setEmail("contragent-42-contragent@mail.com");
        admin.setFullName("Контрагент Контрагентович Контрагентов");
        User user = User.builder()
                .login(admin.getLogin())
                .password(passwordEncoder.encode(admin.getPassword()))
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role("contragent")
                .build();
        userRepository.save(user);
    }


}
