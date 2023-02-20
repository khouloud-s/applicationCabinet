package com.myself.cabinet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myself.cabinet.IntegrationTest;
import com.myself.cabinet.domain.Appointement;
import com.myself.cabinet.domain.Medecin;
import com.myself.cabinet.domain.Patient;
import com.myself.cabinet.domain.ShiftHoraire;
import com.myself.cabinet.repository.AppointementRepository;
import com.myself.cabinet.service.criteria.AppointementCriteria;
import com.myself.cabinet.service.dto.AppointementDTO;
import com.myself.cabinet.service.mapper.AppointementMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link AppointementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppointementResourceIT {

    private static final UUID DEFAULT_USER_UUID = UUID.randomUUID();
    private static final UUID UPDATED_USER_UUID = UUID.randomUUID();

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/appointements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppointementRepository appointementRepository;

    @Autowired
    private AppointementMapper appointementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointementMockMvc;

    private Appointement appointement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointement createEntity(EntityManager em) {
        Appointement appointement = new Appointement().userUuid(DEFAULT_USER_UUID).date(DEFAULT_DATE).isActive(DEFAULT_IS_ACTIVE);
        return appointement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointement createUpdatedEntity(EntityManager em) {
        Appointement appointement = new Appointement().userUuid(UPDATED_USER_UUID).date(UPDATED_DATE).isActive(UPDATED_IS_ACTIVE);
        return appointement;
    }

    @BeforeEach
    public void initTest() {
        appointement = createEntity(em);
    }

    @Test
    @Transactional
    void createAppointement() throws Exception {
        int databaseSizeBeforeCreate = appointementRepository.findAll().size();
        // Create the Appointement
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);
        restAppointementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeCreate + 1);
        Appointement testAppointement = appointementList.get(appointementList.size() - 1);
        assertThat(testAppointement.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);
        assertThat(testAppointement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAppointement.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createAppointementWithExistingId() throws Exception {
        // Create the Appointement with an existing ID
        appointement.setId(1L);
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);

        int databaseSizeBeforeCreate = appointementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointementRepository.findAll().size();
        // set the field null
        appointement.setUserUuid(null);

        // Create the Appointement, which fails.
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);

        restAppointementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppointements() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList
        restAppointementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointement.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getAppointement() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get the appointement
        restAppointementMockMvc
            .perform(get(ENTITY_API_URL_ID, appointement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointement.getId().intValue()))
            .andExpect(jsonPath("$.userUuid").value(DEFAULT_USER_UUID.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getAppointementsByIdFiltering() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        Long id = appointement.getId();

        defaultAppointementShouldBeFound("id.equals=" + id);
        defaultAppointementShouldNotBeFound("id.notEquals=" + id);

        defaultAppointementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppointementShouldNotBeFound("id.greaterThan=" + id);

        defaultAppointementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppointementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppointementsByUserUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where userUuid equals to DEFAULT_USER_UUID
        defaultAppointementShouldBeFound("userUuid.equals=" + DEFAULT_USER_UUID);

        // Get all the appointementList where userUuid equals to UPDATED_USER_UUID
        defaultAppointementShouldNotBeFound("userUuid.equals=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllAppointementsByUserUuidIsInShouldWork() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where userUuid in DEFAULT_USER_UUID or UPDATED_USER_UUID
        defaultAppointementShouldBeFound("userUuid.in=" + DEFAULT_USER_UUID + "," + UPDATED_USER_UUID);

        // Get all the appointementList where userUuid equals to UPDATED_USER_UUID
        defaultAppointementShouldNotBeFound("userUuid.in=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllAppointementsByUserUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where userUuid is not null
        defaultAppointementShouldBeFound("userUuid.specified=true");

        // Get all the appointementList where userUuid is null
        defaultAppointementShouldNotBeFound("userUuid.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointementsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where date equals to DEFAULT_DATE
        defaultAppointementShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the appointementList where date equals to UPDATED_DATE
        defaultAppointementShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppointementsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where date in DEFAULT_DATE or UPDATED_DATE
        defaultAppointementShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the appointementList where date equals to UPDATED_DATE
        defaultAppointementShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppointementsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where date is not null
        defaultAppointementShouldBeFound("date.specified=true");

        // Get all the appointementList where date is null
        defaultAppointementShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointementsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where date is greater than or equal to DEFAULT_DATE
        defaultAppointementShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the appointementList where date is greater than or equal to UPDATED_DATE
        defaultAppointementShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppointementsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where date is less than or equal to DEFAULT_DATE
        defaultAppointementShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the appointementList where date is less than or equal to SMALLER_DATE
        defaultAppointementShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllAppointementsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where date is less than DEFAULT_DATE
        defaultAppointementShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the appointementList where date is less than UPDATED_DATE
        defaultAppointementShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppointementsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where date is greater than DEFAULT_DATE
        defaultAppointementShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the appointementList where date is greater than SMALLER_DATE
        defaultAppointementShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllAppointementsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where isActive equals to DEFAULT_IS_ACTIVE
        defaultAppointementShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the appointementList where isActive equals to UPDATED_IS_ACTIVE
        defaultAppointementShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppointementsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultAppointementShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the appointementList where isActive equals to UPDATED_IS_ACTIVE
        defaultAppointementShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppointementsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        // Get all the appointementList where isActive is not null
        defaultAppointementShouldBeFound("isActive.specified=true");

        // Get all the appointementList where isActive is null
        defaultAppointementShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointementsByMedecinIsEqualToSomething() throws Exception {
        Medecin medecin;
        if (TestUtil.findAll(em, Medecin.class).isEmpty()) {
            appointementRepository.saveAndFlush(appointement);
            medecin = MedecinResourceIT.createEntity(em);
        } else {
            medecin = TestUtil.findAll(em, Medecin.class).get(0);
        }
        em.persist(medecin);
        em.flush();
        appointement.setMedecin(medecin);
        appointementRepository.saveAndFlush(appointement);
        Long medecinId = medecin.getId();

        // Get all the appointementList where medecin equals to medecinId
        defaultAppointementShouldBeFound("medecinId.equals=" + medecinId);

        // Get all the appointementList where medecin equals to (medecinId + 1)
        defaultAppointementShouldNotBeFound("medecinId.equals=" + (medecinId + 1));
    }

    @Test
    @Transactional
    void getAllAppointementsByPatientIsEqualToSomething() throws Exception {
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            appointementRepository.saveAndFlush(appointement);
            patient = PatientResourceIT.createEntity(em);
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        em.persist(patient);
        em.flush();
        appointement.setPatient(patient);
        appointementRepository.saveAndFlush(appointement);
        Long patientId = patient.getId();

        // Get all the appointementList where patient equals to patientId
        defaultAppointementShouldBeFound("patientId.equals=" + patientId);

        // Get all the appointementList where patient equals to (patientId + 1)
        defaultAppointementShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    @Test
    @Transactional
    void getAllAppointementsByShiftHoraireIsEqualToSomething() throws Exception {
        ShiftHoraire shiftHoraire;
        if (TestUtil.findAll(em, ShiftHoraire.class).isEmpty()) {
            appointementRepository.saveAndFlush(appointement);
            shiftHoraire = ShiftHoraireResourceIT.createEntity(em);
        } else {
            shiftHoraire = TestUtil.findAll(em, ShiftHoraire.class).get(0);
        }
        em.persist(shiftHoraire);
        em.flush();
        appointement.setShiftHoraire(shiftHoraire);
        appointementRepository.saveAndFlush(appointement);
        Long shiftHoraireId = shiftHoraire.getId();

        // Get all the appointementList where shiftHoraire equals to shiftHoraireId
        defaultAppointementShouldBeFound("shiftHoraireId.equals=" + shiftHoraireId);

        // Get all the appointementList where shiftHoraire equals to (shiftHoraireId + 1)
        defaultAppointementShouldNotBeFound("shiftHoraireId.equals=" + (shiftHoraireId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppointementShouldBeFound(String filter) throws Exception {
        restAppointementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointement.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restAppointementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppointementShouldNotBeFound(String filter) throws Exception {
        restAppointementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppointementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppointement() throws Exception {
        // Get the appointement
        restAppointementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppointement() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();

        // Update the appointement
        Appointement updatedAppointement = appointementRepository.findById(appointement.getId()).get();
        // Disconnect from session so that the updates on updatedAppointement are not directly saved in db
        em.detach(updatedAppointement);
        updatedAppointement.userUuid(UPDATED_USER_UUID).date(UPDATED_DATE).isActive(UPDATED_IS_ACTIVE);
        AppointementDTO appointementDTO = appointementMapper.toDto(updatedAppointement);

        restAppointementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
        Appointement testAppointement = appointementList.get(appointementList.size() - 1);
        assertThat(testAppointement.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testAppointement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAppointement.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingAppointement() throws Exception {
        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();
        appointement.setId(count.incrementAndGet());

        // Create the Appointement
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppointement() throws Exception {
        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();
        appointement.setId(count.incrementAndGet());

        // Create the Appointement
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppointement() throws Exception {
        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();
        appointement.setId(count.incrementAndGet());

        // Create the Appointement
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppointementWithPatch() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();

        // Update the appointement using partial update
        Appointement partialUpdatedAppointement = new Appointement();
        partialUpdatedAppointement.setId(appointement.getId());

        partialUpdatedAppointement.isActive(UPDATED_IS_ACTIVE);

        restAppointementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointement))
            )
            .andExpect(status().isOk());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
        Appointement testAppointement = appointementList.get(appointementList.size() - 1);
        assertThat(testAppointement.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);
        assertThat(testAppointement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAppointement.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateAppointementWithPatch() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();

        // Update the appointement using partial update
        Appointement partialUpdatedAppointement = new Appointement();
        partialUpdatedAppointement.setId(appointement.getId());

        partialUpdatedAppointement.userUuid(UPDATED_USER_UUID).date(UPDATED_DATE).isActive(UPDATED_IS_ACTIVE);

        restAppointementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointement))
            )
            .andExpect(status().isOk());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
        Appointement testAppointement = appointementList.get(appointementList.size() - 1);
        assertThat(testAppointement.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testAppointement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAppointement.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingAppointement() throws Exception {
        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();
        appointement.setId(count.incrementAndGet());

        // Create the Appointement
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppointement() throws Exception {
        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();
        appointement.setId(count.incrementAndGet());

        // Create the Appointement
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppointement() throws Exception {
        int databaseSizeBeforeUpdate = appointementRepository.findAll().size();
        appointement.setId(count.incrementAndGet());

        // Create the Appointement
        AppointementDTO appointementDTO = appointementMapper.toDto(appointement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointement in the database
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppointement() throws Exception {
        // Initialize the database
        appointementRepository.saveAndFlush(appointement);

        int databaseSizeBeforeDelete = appointementRepository.findAll().size();

        // Delete the appointement
        restAppointementMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appointement> appointementList = appointementRepository.findAll();
        assertThat(appointementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
