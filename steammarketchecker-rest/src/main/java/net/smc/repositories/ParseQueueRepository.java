package net.smc.repositories;

import net.smc.entities.ActiveName;
import net.smc.entities.ParseQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParseQueueRepository extends JpaRepository<ParseQueue, Long> {

    public List<ParseQueue> findAllByArchiveOrderByImportanceDesc(boolean archive);

    public List<ParseQueue> findAllByArchiveOrderByImportanceDescIdAsc(boolean archive); //чтобы какую-то старую задачу не

}
