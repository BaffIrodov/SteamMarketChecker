package net.smc.repositories;

import net.smc.entities.ParseQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParseQueueRepository extends JpaRepository<ParseQueue, Long> {
}
