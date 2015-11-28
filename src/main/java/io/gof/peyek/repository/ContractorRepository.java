package io.gof.peyek.repository;

import io.gof.peyek.domain.Contractor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Contractor entity.
 */
public interface ContractorRepository extends JpaRepository<Contractor,Long> {

    @Query("select contractor from Contractor contractor where contractor.user.login = ?#{principal.username}")
    List<Contractor> findByUserIsCurrentUser();

}
