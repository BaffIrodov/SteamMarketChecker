package net.smc.dto;

import net.smc.entities.User;

import lombok.*;


@Data
@NoArgsConstructor
public class UserDto {
    public String login;
    private String fullName;
    private String email;
    private String role;

    public UserDto(User user) {
        this.login = user.getLogin();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
    }

    @Deprecated
    public UserDto UserDtoWithTelegram(User user) {
        this.login = user.getLogin();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        return this;
    }
}