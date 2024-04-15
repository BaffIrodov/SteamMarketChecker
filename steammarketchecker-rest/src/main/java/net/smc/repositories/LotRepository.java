package net.smc.repositories;

import net.smc.entities.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    List<Lot> findAllByLotParseTarget(String lotParseTarget);

}
