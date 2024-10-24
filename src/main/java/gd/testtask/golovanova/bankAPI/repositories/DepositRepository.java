package gd.testtask.golovanova.bankAPI.repositories;

import gd.testtask.golovanova.bankAPI.models.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer> {
}
