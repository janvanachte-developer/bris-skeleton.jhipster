package eu.europa.ec.digit.bris.common.web.rest;

import eu.europa.ec.digit.bris.common.SkeletonApp;

import eu.europa.ec.digit.bris.common.domain.Parameter;
import eu.europa.ec.digit.bris.common.repository.ParameterRepository;
import eu.europa.ec.digit.bris.common.repository.search.ParameterSearchRepository;
import eu.europa.ec.digit.bris.common.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static eu.europa.ec.digit.bris.common.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParameterResource REST controller.
 *
 * @see ParameterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SkeletonApp.class)
public class ParameterResourceIntTest {

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATION_USER = "AAAAAAAAAA";
    private static final String UPDATED_CREATION_USER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LAST_MODIFIED_USER = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_USER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterSearchRepository parameterSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParameterMockMvc;

    private Parameter parameter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParameterResource parameterResource = new ParameterResource(parameterRepository, parameterSearchRepository);
        this.restParameterMockMvc = MockMvcBuilders.standaloneSetup(parameterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .domain(DEFAULT_DOMAIN)
            .parameter(DEFAULT_PARAMETER)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .value(DEFAULT_VALUE)
            .creationUser(DEFAULT_CREATION_USER)
            .creationDateTime(DEFAULT_CREATION_DATE_TIME)
            .lastModifiedUser(DEFAULT_LAST_MODIFIED_USER)
            .lastModifiedDateTime(DEFAULT_LAST_MODIFIED_DATE_TIME);
        return parameter;
    }

    @Before
    public void initTest() {
        parameterSearchRepository.deleteAll();
        parameter = createEntity(em);
    }

    @Test
    @Transactional
    public void createParameter() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter
        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate + 1);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getDomain()).isEqualTo(DEFAULT_DOMAIN);
        assertThat(testParameter.getParameter()).isEqualTo(DEFAULT_PARAMETER);
        assertThat(testParameter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParameter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testParameter.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testParameter.getCreationUser()).isEqualTo(DEFAULT_CREATION_USER);
        assertThat(testParameter.getCreationDateTime()).isEqualTo(DEFAULT_CREATION_DATE_TIME);
        assertThat(testParameter.getLastModifiedUser()).isEqualTo(DEFAULT_LAST_MODIFIED_USER);
        assertThat(testParameter.getLastModifiedDateTime()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE_TIME);

        // Validate the Parameter in Elasticsearch
        Parameter parameterEs = parameterSearchRepository.findOne(testParameter.getId());
        assertThat(parameterEs).isEqualToComparingFieldByField(testParameter);
    }

    @Test
    @Transactional
    public void createParameterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter with an existing ID
        parameter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParameters() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList
        restParameterMockMvc.perform(get("/api/parameters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].parameter").value(hasItem(DEFAULT_PARAMETER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].creationUser").value(hasItem(DEFAULT_CREATION_USER.toString())))
            .andExpect(jsonPath("$.[*].creationDateTime").value(hasItem(sameInstant(DEFAULT_CREATION_DATE_TIME))))
            .andExpect(jsonPath("$.[*].lastModifiedUser").value(hasItem(DEFAULT_LAST_MODIFIED_USER.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDateTime").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE_TIME))));
    }

    @Test
    @Transactional
    public void getParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()))
            .andExpect(jsonPath("$.parameter").value(DEFAULT_PARAMETER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.creationUser").value(DEFAULT_CREATION_USER.toString()))
            .andExpect(jsonPath("$.creationDateTime").value(sameInstant(DEFAULT_CREATION_DATE_TIME)))
            .andExpect(jsonPath("$.lastModifiedUser").value(DEFAULT_LAST_MODIFIED_USER.toString()))
            .andExpect(jsonPath("$.lastModifiedDateTime").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingParameter() throws Exception {
        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        parameterSearchRepository.save(parameter);
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Update the parameter
        Parameter updatedParameter = parameterRepository.findOne(parameter.getId());
        updatedParameter
            .domain(UPDATED_DOMAIN)
            .parameter(UPDATED_PARAMETER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .value(UPDATED_VALUE)
            .creationUser(UPDATED_CREATION_USER)
            .creationDateTime(UPDATED_CREATION_DATE_TIME)
            .lastModifiedUser(UPDATED_LAST_MODIFIED_USER)
            .lastModifiedDateTime(UPDATED_LAST_MODIFIED_DATE_TIME);

        restParameterMockMvc.perform(put("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParameter)))
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testParameter.getParameter()).isEqualTo(UPDATED_PARAMETER);
        assertThat(testParameter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParameter.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testParameter.getCreationUser()).isEqualTo(UPDATED_CREATION_USER);
        assertThat(testParameter.getCreationDateTime()).isEqualTo(UPDATED_CREATION_DATE_TIME);
        assertThat(testParameter.getLastModifiedUser()).isEqualTo(UPDATED_LAST_MODIFIED_USER);
        assertThat(testParameter.getLastModifiedDateTime()).isEqualTo(UPDATED_LAST_MODIFIED_DATE_TIME);

        // Validate the Parameter in Elasticsearch
        Parameter parameterEs = parameterSearchRepository.findOne(testParameter.getId());
        assertThat(parameterEs).isEqualToComparingFieldByField(testParameter);
    }

    @Test
    @Transactional
    public void updateNonExistingParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Create the Parameter

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParameterMockMvc.perform(put("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        parameterSearchRepository.save(parameter);
        int databaseSizeBeforeDelete = parameterRepository.findAll().size();

        // Get the parameter
        restParameterMockMvc.perform(delete("/api/parameters/{id}", parameter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean parameterExistsInEs = parameterSearchRepository.exists(parameter.getId());
        assertThat(parameterExistsInEs).isFalse();

        // Validate the database is empty
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        parameterSearchRepository.save(parameter);

        // Search the parameter
        restParameterMockMvc.perform(get("/api/_search/parameters?query=id:" + parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].parameter").value(hasItem(DEFAULT_PARAMETER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].creationUser").value(hasItem(DEFAULT_CREATION_USER.toString())))
            .andExpect(jsonPath("$.[*].creationDateTime").value(hasItem(sameInstant(DEFAULT_CREATION_DATE_TIME))))
            .andExpect(jsonPath("$.[*].lastModifiedUser").value(hasItem(DEFAULT_LAST_MODIFIED_USER.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDateTime").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE_TIME))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parameter.class);
        Parameter parameter1 = new Parameter();
        parameter1.setId(1L);
        Parameter parameter2 = new Parameter();
        parameter2.setId(parameter1.getId());
        assertThat(parameter1).isEqualTo(parameter2);
        parameter2.setId(2L);
        assertThat(parameter1).isNotEqualTo(parameter2);
        parameter1.setId(null);
        assertThat(parameter1).isNotEqualTo(parameter2);
    }
}
