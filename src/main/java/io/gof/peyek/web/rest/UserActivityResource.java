package io.gof.peyek.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.gof.peyek.domain.UserActivity;
import io.gof.peyek.repository.UserActivityRepository;
import io.gof.peyek.web.rest.util.HeaderUtil;
import io.gof.peyek.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserActivity.
 */
@RestController
@RequestMapping("/api")
public class UserActivityResource {

    private final Logger log = LoggerFactory.getLogger(UserActivityResource.class);

    @Inject
    private UserActivityRepository userActivityRepository;

    /**
     * POST  /userActivitys -> Create a new userActivity.
     */
    @RequestMapping(value = "/userActivitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserActivity> createUserActivity(@Valid @RequestBody UserActivity userActivity) throws URISyntaxException {
        log.debug("REST request to save UserActivity : {}", userActivity);
        if (userActivity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userActivity cannot already have an ID").body(null);
        }
        UserActivity result = userActivityRepository.save(userActivity);
        return ResponseEntity.created(new URI("/api/userActivitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userActivity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userActivitys -> Updates an existing userActivity.
     */
    @RequestMapping(value = "/userActivitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserActivity> updateUserActivity(@Valid @RequestBody UserActivity userActivity) throws URISyntaxException {
        log.debug("REST request to update UserActivity : {}", userActivity);
        if (userActivity.getId() == null) {
            return createUserActivity(userActivity);
        }
        UserActivity result = userActivityRepository.save(userActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userActivity", userActivity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userActivitys -> get all the userActivitys.
     */
    @RequestMapping(value = "/userActivitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserActivity>> getAllUserActivitys(Pageable pageable)
        throws URISyntaxException {
        Page<UserActivity> page = userActivityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userActivitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userActivitys/:id -> get the "id" userActivity.
     */
    @RequestMapping(value = "/userActivitys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserActivity> getUserActivity(@PathVariable Long id) {
        log.debug("REST request to get UserActivity : {}", id);
        return Optional.ofNullable(userActivityRepository.findOne(id))
            .map(userActivity -> new ResponseEntity<>(
                userActivity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userActivitys/:id -> delete the "id" userActivity.
     */
    @RequestMapping(value = "/userActivitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserActivity(@PathVariable Long id) {
        log.debug("REST request to delete UserActivity : {}", id);
        userActivityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userActivity", id.toString())).build();
    }
}
