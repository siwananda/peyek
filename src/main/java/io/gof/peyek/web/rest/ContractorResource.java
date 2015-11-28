package io.gof.peyek.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.gof.peyek.domain.Contractor;
import io.gof.peyek.repository.ContractorRepository;
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
 * REST controller for managing Contractor.
 */
@RestController
@RequestMapping("/api")
public class ContractorResource {

    private final Logger log = LoggerFactory.getLogger(ContractorResource.class);

    @Inject
    private ContractorRepository contractorRepository;

    /**
     * POST  /contractors -> Create a new contractor.
     */
    @RequestMapping(value = "/contractors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contractor> createContractor(@Valid @RequestBody Contractor contractor) throws URISyntaxException {
        log.debug("REST request to save Contractor : {}", contractor);
        if (contractor.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new contractor cannot already have an ID").body(null);
        }
        Contractor result = contractorRepository.save(contractor);
        return ResponseEntity.created(new URI("/api/contractors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contractor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contractors -> Updates an existing contractor.
     */
    @RequestMapping(value = "/contractors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contractor> updateContractor(@Valid @RequestBody Contractor contractor) throws URISyntaxException {
        log.debug("REST request to update Contractor : {}", contractor);
        if (contractor.getId() == null) {
            return createContractor(contractor);
        }
        Contractor result = contractorRepository.save(contractor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contractor", contractor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contractors -> get all the contractors.
     */
    @RequestMapping(value = "/contractors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Contractor>> getAllContractors(Pageable pageable)
        throws URISyntaxException {
        Page<Contractor> page = contractorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contractors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contractors/:id -> get the "id" contractor.
     */
    @RequestMapping(value = "/contractors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contractor> getContractor(@PathVariable Long id) {
        log.debug("REST request to get Contractor : {}", id);
        return Optional.ofNullable(contractorRepository.findOne(id))
            .map(contractor -> new ResponseEntity<>(
                contractor,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contractors/:id -> delete the "id" contractor.
     */
    @RequestMapping(value = "/contractors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContractor(@PathVariable Long id) {
        log.debug("REST request to delete Contractor : {}", id);
        contractorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contractor", id.toString())).build();
    }
}
