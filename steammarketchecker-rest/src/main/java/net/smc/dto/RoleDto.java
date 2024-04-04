package net.smc.dto;

import net.smc.entities.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private String description;


    public RoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
    }
    
}
