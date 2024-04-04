package net.smc.controllers;


import lombok.RequiredArgsConstructor;
import net.smc.services.UserService;
import net.smc.dto.PasswordChangeRequestDto;
import net.smc.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("all")
    public List<UserDto> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("{login}/roles")
    public List<String> getUserRoles(@PathVariable String login) {
        return Collections.singletonList(userService.getUserRole(login));
    }

    @PutMapping("update")
    public UserDto updateUser(@RequestBody UserDto user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("delete/{login}")
    public void deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
    }

    @PostMapping("currentUser")
    public UserDto getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }

    @PostMapping("user-data-change")
    public void fullNameChange(@RequestBody UserDto userWithChanges) {
        userService.userDataChange(userWithChanges);
    }

    @PostMapping("password-change")
    public Boolean passwordChange(@RequestBody PasswordChangeRequestDto request) {
        return userService.passwordChange(request.getUser().getLogin(), request.getOldPassword(), request.getNewPassword());
    }

    @PostMapping("recover/send-recover-letter")
    public Boolean sendRecoverLetter(@RequestBody String email) {
        return true;
    }

    @PostMapping("recover/verify-recover-code")
    public Boolean verifyRecoverCOde(@RequestBody String recoverCode) {
        return true;
    }

    @PostMapping("recover/save-new-password")
    public Boolean saveNewPassword(@RequestBody String newPassword) {
        return true;
    }
}
