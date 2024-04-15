package net.smc.repositories;

import net.smc.entities.ActualCurrencyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActualCurrencyRelationRepository extends JpaRepository<ActualCurrencyRelation, Long> {

    public List<ActualCurrencyRelation> findAllByArchive(boolean archive);
}
