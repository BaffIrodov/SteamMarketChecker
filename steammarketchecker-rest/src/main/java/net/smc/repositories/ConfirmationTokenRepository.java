package net.smc.repositories;

import net.smc.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(UUID confirmationToken);
}
