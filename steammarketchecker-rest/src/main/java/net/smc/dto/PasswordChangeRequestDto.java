package net.smc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PasswordChangeRequestDto {
    public String oldPassword;
    public String newPassword;
    public UserDto user;
}