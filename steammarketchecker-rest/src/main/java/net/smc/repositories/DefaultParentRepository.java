package net.smc.repositories;

import net.smc.entities.DefaultParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultParentRepository extends JpaRepository<DefaultParent, Long> {
}
