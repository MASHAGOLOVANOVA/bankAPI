package gd.testtask.golovanova.bankAPI.repositories;

import gd.testtask.golovanova.bankAPI.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
}
