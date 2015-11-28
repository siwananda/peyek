package io.gof.peyek.web.rest;

import io.gof.peyek.Application;
import io.gof.peyek.domain.Contractor;
import io.gof.peyek.repository.ContractorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ContractorResource REST controller.
 *
 * @see ContractorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ContractorResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ContractorRepository contractorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContractorMockMvc;

    private Contractor contractor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContractorResource contractorResource = new ContractorResource();
        ReflectionTestUtils.setField(contractorResource, "contractorRepository", contractorRepository);
        this.restContractorMockMvc = MockMvcBuilders.standaloneSetup(contractorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        contractor = new Contractor();
        contractor.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createContractor() throws Exception {
        int databaseSizeBeforeCreate = contractorRepository.findAll().size();

        // Create the Contractor

        restContractorMockMvc.perform(post("/api/contractors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractor)))
                .andExpect(status().isCreated());

        // Validate the Contractor in the database
        List<Contractor> contractors = contractorRepository.findAll();
        assertThat(contractors).hasSize(databaseSizeBeforeCreate + 1);
        Contractor testContractor = contractors.get(contractors.size() - 1);
        assertThat(testContractor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractorRepository.findAll().size();
        // set the field null
        contractor.setName(null);

        // Create the Contractor, which fails.

        restContractorMockMvc.perform(post("/api/contractors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractor)))
                .andExpect(status().isBadRequest());

        List<Contractor> contractors = contractorRepository.findAll();
        assertThat(contractors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContractors() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractors
        restContractorMockMvc.perform(get("/api/contractors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contractor.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get the contractor
        restContractorMockMvc.perform(get("/api/contractors/{id}", contractor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contractor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContractor() throws Exception {
        // Get the contractor
        restContractorMockMvc.perform(get("/api/contractors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

		int databaseSizeBeforeUpdate = contractorRepository.findAll().size();

        // Update the contractor
        contractor.setName(UPDATED_NAME);

        restContractorMockMvc.perform(put("/api/contractors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractor)))
                .andExpect(status().isOk());

        // Validate the Contractor in the database
        List<Contractor> contractors = contractorRepository.findAll();
        assertThat(contractors).hasSize(databaseSizeBeforeUpdate);
        Contractor testContractor = contractors.get(contractors.size() - 1);
        assertThat(testContractor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

		int databaseSizeBeforeDelete = contractorRepository.findAll().size();

        // Get the contractor
        restContractorMockMvc.perform(delete("/api/contractors/{id}", contractor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Contractor> contractors = contractorRepository.findAll();
        assertThat(contractors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
