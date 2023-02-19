package com.myself.cabinet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myself.cabinet.IntegrationTest;
import com.myself.cabinet.domain.Appointement;
import com.myself.cabinet.domain.Patient;
import com.myself.cabinet.repository.PatientRepository;
import com.myself.cabinet.service.criteria.PatientCriteria;
import com.myself.cabinet.service.dto.PatientDTO;
import com.myself.cabinet.service.mapper.PatientMapper;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientResourceIT {

    private static final UUID DEFAULT_USER_UUID = UUID.randomUUID();
    private static final UUID UPDATED_USER_UUID = UUID.randomUUID();

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SCAN_ORDONNANCE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SCAN_ORDONNANCE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SCAN_ORDONNANCE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SCAN_ORDONNANCE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .userUuid(DEFAULT_USER_UUID)
            .fullName(DEFAULT_FULL_NAME)
            .email(DEFAULT_EMAIL)
            .scanOrdonnance(DEFAULT_SCAN_ORDONNANCE)
            .scanOrdonnanceContentType(DEFAULT_SCAN_ORDONNANCE_CONTENT_TYPE);
        return patient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .userUuid(UPDATED_USER_UUID)
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .scanOrdonnance(UPDATED_SCAN_ORDONNANCE)
            .scanOrdonnanceContentType(UPDATED_SCAN_ORDONNANCE_CONTENT_TYPE);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);
        assertThat(testPatient.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testPatient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPatient.getScanOrdonnance()).isEqualTo(DEFAULT_SCAN_ORDONNANCE);
        assertThat(testPatient.getScanOrdonnanceContentType()).isEqualTo(DEFAULT_SCAN_ORDONNANCE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId(1L);
        PatientDTO patientDTO = patientMapper.toDto(patient);

        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setUserUuid(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].scanOrdonnanceContentType").value(hasItem(DEFAULT_SCAN_ORDONNANCE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].scanOrdonnance").value(hasItem(Base64Utils.encodeToString(DEFAULT_SCAN_ORDONNANCE))));
    }

    @Test
    @Transactional
    void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc
            .perform(get(ENTITY_API_URL_ID, patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.userUuid").value(DEFAULT_USER_UUID.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.scanOrdonnanceContentType").value(DEFAULT_SCAN_ORDONNANCE_CONTENT_TYPE))
            .andExpect(jsonPath("$.scanOrdonnance").value(Base64Utils.encodeToString(DEFAULT_SCAN_ORDONNANCE)));
    }

    @Test
    @Transactional
    void getPatientsByIdFiltering() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        Long id = patient.getId();

        defaultPatientShouldBeFound("id.equals=" + id);
        defaultPatientShouldNotBeFound("id.notEquals=" + id);

        defaultPatientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPatientsByUserUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where userUuid equals to DEFAULT_USER_UUID
        defaultPatientShouldBeFound("userUuid.equals=" + DEFAULT_USER_UUID);

        // Get all the patientList where userUuid equals to UPDATED_USER_UUID
        defaultPatientShouldNotBeFound("userUuid.equals=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllPatientsByUserUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where userUuid not equals to DEFAULT_USER_UUID
        defaultPatientShouldNotBeFound("userUuid.notEquals=" + DEFAULT_USER_UUID);

        // Get all the patientList where userUuid not equals to UPDATED_USER_UUID
        defaultPatientShouldBeFound("userUuid.notEquals=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllPatientsByUserUuidIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where userUuid in DEFAULT_USER_UUID or UPDATED_USER_UUID
        defaultPatientShouldBeFound("userUuid.in=" + DEFAULT_USER_UUID + "," + UPDATED_USER_UUID);

        // Get all the patientList where userUuid equals to UPDATED_USER_UUID
        defaultPatientShouldNotBeFound("userUuid.in=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllPatientsByUserUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where userUuid is not null
        defaultPatientShouldBeFound("userUuid.specified=true");

        // Get all the patientList where userUuid is null
        defaultPatientShouldNotBeFound("userUuid.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName equals to DEFAULT_FULL_NAME
        defaultPatientShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName equals to UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName not equals to DEFAULT_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName not equals to UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the patientList where fullName equals to UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName is not null
        defaultPatientShouldBeFound("fullName.specified=true");

        // Get all the patientList where fullName is null
        defaultPatientShouldNotBeFound("fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName contains DEFAULT_FULL_NAME
        defaultPatientShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName contains UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName does not contain DEFAULT_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName does not contain UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email equals to DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email not equals to DEFAULT_EMAIL
        defaultPatientShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the patientList where email not equals to UPDATED_EMAIL
        defaultPatientShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPatientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email is not null
        defaultPatientShouldBeFound("email.specified=true");

        // Get all the patientList where email is null
        defaultPatientShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email contains DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the patientList where email contains UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email does not contain DEFAULT_EMAIL
        defaultPatientShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the patientList where email does not contain UPDATED_EMAIL
        defaultPatientShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPatientsByAppointementsIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Appointement appointements;
        if (TestUtil.findAll(em, Appointement.class).isEmpty()) {
            appointements = AppointementResourceIT.createEntity(em);
            em.persist(appointements);
            em.flush();
        } else {
            appointements = TestUtil.findAll(em, Appointement.class).get(0);
        }
        em.persist(appointements);
        em.flush();
        patient.addAppointements(appointements);
        patientRepository.saveAndFlush(patient);
        Long appointementsId = appointements.getId();

        // Get all the patientList where appointements equals to appointementsId
        defaultPatientShouldBeFound("appointementsId.equals=" + appointementsId);

        // Get all the patientList where appointements equals to (appointementsId + 1)
        defaultPatientShouldNotBeFound("appointementsId.equals=" + (appointementsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].scanOrdonnanceContentType").value(hasItem(DEFAULT_SCAN_ORDONNANCE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].scanOrdonnance").value(hasItem(Base64Utils.encodeToString(DEFAULT_SCAN_ORDONNANCE))));

        // Check, that the count call also returns 1
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .userUuid(UPDATED_USER_UUID)
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .scanOrdonnance(UPDATED_SCAN_ORDONNANCE)
            .scanOrdonnanceContentType(UPDATED_SCAN_ORDONNANCE_CONTENT_TYPE);
        PatientDTO patientDTO = patientMapper.toDto(updatedPatient);

        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testPatient.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getScanOrdonnance()).isEqualTo(UPDATED_SCAN_ORDONNANCE);
        assertThat(testPatient.getScanOrdonnanceContentType()).isEqualTo(UPDATED_SCAN_ORDONNANCE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .userUuid(UPDATED_USER_UUID)
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .scanOrdonnance(UPDATED_SCAN_ORDONNANCE)
            .scanOrdonnanceContentType(UPDATED_SCAN_ORDONNANCE_CONTENT_TYPE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testPatient.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getScanOrdonnance()).isEqualTo(UPDATED_SCAN_ORDONNANCE);
        assertThat(testPatient.getScanOrdonnanceContentType()).isEqualTo(UPDATED_SCAN_ORDONNANCE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .userUuid(UPDATED_USER_UUID)
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .scanOrdonnance(UPDATED_SCAN_ORDONNANCE)
            .scanOrdonnanceContentType(UPDATED_SCAN_ORDONNANCE_CONTENT_TYPE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testPatient.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatient.getScanOrdonnance()).isEqualTo(UPDATED_SCAN_ORDONNANCE);
        assertThat(testPatient.getScanOrdonnanceContentType()).isEqualTo(UPDATED_SCAN_ORDONNANCE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, patient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
