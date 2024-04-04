package net.smc.controllers;


import net.smc.repositories.UserRepository;
import net.smc.entities.User;
import net.smc.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void register(@RequestBody User newUser) {
        User user = User.builder()
                .login(newUser.getLogin())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .email(newUser.getEmail())
                .fullName(newUser.getFullName())
                .role("user")
                .build();
        if(!userService.isValidatedOfChiefCreate(user) || !userService.isValidatedOfDublicateCreate(user)) {
            //никогда не попадем сюда
            throw new RuntimeException("Нельзя создавать второго шефа!");
        }
        userRepository.save(user);
    }

    @PostMapping("login")
    public Boolean getLogin() {
        return true;
    }
}
