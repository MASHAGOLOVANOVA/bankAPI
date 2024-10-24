package gd.testtask.golovanova.bankAPI.repositories;

import gd.testtask.golovanova.bankAPI.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
