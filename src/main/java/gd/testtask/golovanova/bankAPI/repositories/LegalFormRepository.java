package gd.testtask.golovanova.bankAPI.repositories;

import gd.testtask.golovanova.bankAPI.models.LegalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalFormRepository extends JpaRepository<LegalForm, Integer> {
}
