package net.smc.repositories;

import net.smc.entities.SteamItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SteamItemRepository extends JpaRepository<SteamItem, Long> {
}
