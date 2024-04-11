package net.smc.repositories;

import net.smc.entities.ActiveName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveNameRepository extends JpaRepository<ActiveName, Long> {

    public List<ActiveName> findAllByArchive(boolean archive);

}
