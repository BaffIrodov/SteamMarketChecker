package net.smc.repositories;

import net.smc.entities.LotSticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotStickerRepository extends JpaRepository<LotSticker, Long> {
}
