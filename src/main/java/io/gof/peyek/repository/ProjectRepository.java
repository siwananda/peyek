package io.gof.peyek.repository;

import io.gof.peyek.domain.Project;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("select project from Project project where project.owner.login = ?#{principal.username}")
    List<Project> findByOwnerIsCurrentUser();

    @Query("select project from Project project where project.winner.login = ?#{principal.username}")
    List<Project> findByWinnerIsCurrentUser();

    @Query("select distinct project from Project project left join fetch project.bidders")
    List<Project> findAllWithEagerRelationships();

    @Query("select project from Project project left join fetch project.bidders where project.id =:id")
    Project findOneWithEagerRelationships(@Param("id") Long id);

}
