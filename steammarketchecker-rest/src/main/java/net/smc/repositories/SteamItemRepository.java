package net.smc.repositories;

import net.smc.entities.SteamItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamItemRepository extends JpaRepository<SteamItem, Long> {

    public List<SteamItem> findAllByName(String name);
}
