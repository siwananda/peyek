package io.gof.peyek.repository;

import io.gof.peyek.domain.UserActivity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserActivity entity.
 */
public interface UserActivityRepository extends JpaRepository<UserActivity,Long> {

    @Query("select userActivity from UserActivity userActivity where userActivity.source.login = ?#{principal.username}")
    List<UserActivity> findBySourceIsCurrentUser();

}
