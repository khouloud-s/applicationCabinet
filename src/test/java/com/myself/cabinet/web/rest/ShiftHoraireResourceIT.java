package com.myself.cabinet.web.rest;

import static com.myself.cabinet.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myself.cabinet.IntegrationTest;
import com.myself.cabinet.domain.Appointement;
import com.myself.cabinet.domain.ShiftHoraire;
import com.myself.cabinet.repository.ShiftHoraireRepository;
import com.myself.cabinet.service.criteria.ShiftHoraireCriteria;
import com.myself.cabinet.service.dto.ShiftHoraireDTO;
import com.myself.cabinet.service.mapper.ShiftHoraireMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link ShiftHoraireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShiftHoraireResourceIT {

    private static final UUID DEFAULT_USER_UUID = UUID.randomUUID();
    private static final UUID UPDATED_USER_UUID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_TIME_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/shift-horaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShiftHoraireRepository shiftHoraireRepository;

    @Autowired
    private ShiftHoraireMapper shiftHoraireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShiftHoraireMockMvc;

    private ShiftHoraire shiftHoraire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShiftHoraire createEntity(EntityManager em) {
        ShiftHoraire shiftHoraire = new ShiftHoraire().userUuid(DEFAULT_USER_UUID).timeStart(DEFAULT_TIME_START).timeEnd(DEFAULT_TIME_END);
        return shiftHoraire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShiftHoraire createUpdatedEntity(EntityManager em) {
        ShiftHoraire shiftHoraire = new ShiftHoraire().userUuid(UPDATED_USER_UUID).timeStart(UPDATED_TIME_START).timeEnd(UPDATED_TIME_END);
        return shiftHoraire;
    }

    @BeforeEach
    public void initTest() {
        shiftHoraire = createEntity(em);
    }

    @Test
    @Transactional
    void createShiftHoraire() throws Exception {
        int databaseSizeBeforeCreate = shiftHoraireRepository.findAll().size();
        // Create the ShiftHoraire
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);
        restShiftHoraireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeCreate + 1);
        ShiftHoraire testShiftHoraire = shiftHoraireList.get(shiftHoraireList.size() - 1);
        assertThat(testShiftHoraire.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);
        assertThat(testShiftHoraire.getTimeStart()).isEqualTo(DEFAULT_TIME_START);
        assertThat(testShiftHoraire.getTimeEnd()).isEqualTo(DEFAULT_TIME_END);
    }

    @Test
    @Transactional
    void createShiftHoraireWithExistingId() throws Exception {
        // Create the ShiftHoraire with an existing ID
        shiftHoraire.setId(1L);
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);

        int databaseSizeBeforeCreate = shiftHoraireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShiftHoraireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = shiftHoraireRepository.findAll().size();
        // set the field null
        shiftHoraire.setUserUuid(null);

        // Create the ShiftHoraire, which fails.
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);

        restShiftHoraireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShiftHoraires() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList
        restShiftHoraireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shiftHoraire.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].timeStart").value(hasItem(sameInstant(DEFAULT_TIME_START))))
            .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(sameInstant(DEFAULT_TIME_END))));
    }

    @Test
    @Transactional
    void getShiftHoraire() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get the shiftHoraire
        restShiftHoraireMockMvc
            .perform(get(ENTITY_API_URL_ID, shiftHoraire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shiftHoraire.getId().intValue()))
            .andExpect(jsonPath("$.userUuid").value(DEFAULT_USER_UUID.toString()))
            .andExpect(jsonPath("$.timeStart").value(sameInstant(DEFAULT_TIME_START)))
            .andExpect(jsonPath("$.timeEnd").value(sameInstant(DEFAULT_TIME_END)));
    }

    @Test
    @Transactional
    void getShiftHorairesByIdFiltering() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        Long id = shiftHoraire.getId();

        defaultShiftHoraireShouldBeFound("id.equals=" + id);
        defaultShiftHoraireShouldNotBeFound("id.notEquals=" + id);

        defaultShiftHoraireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShiftHoraireShouldNotBeFound("id.greaterThan=" + id);

        defaultShiftHoraireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShiftHoraireShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByUserUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where userUuid equals to DEFAULT_USER_UUID
        defaultShiftHoraireShouldBeFound("userUuid.equals=" + DEFAULT_USER_UUID);

        // Get all the shiftHoraireList where userUuid equals to UPDATED_USER_UUID
        defaultShiftHoraireShouldNotBeFound("userUuid.equals=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByUserUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where userUuid not equals to DEFAULT_USER_UUID
        defaultShiftHoraireShouldNotBeFound("userUuid.notEquals=" + DEFAULT_USER_UUID);

        // Get all the shiftHoraireList where userUuid not equals to UPDATED_USER_UUID
        defaultShiftHoraireShouldBeFound("userUuid.notEquals=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByUserUuidIsInShouldWork() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where userUuid in DEFAULT_USER_UUID or UPDATED_USER_UUID
        defaultShiftHoraireShouldBeFound("userUuid.in=" + DEFAULT_USER_UUID + "," + UPDATED_USER_UUID);

        // Get all the shiftHoraireList where userUuid equals to UPDATED_USER_UUID
        defaultShiftHoraireShouldNotBeFound("userUuid.in=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByUserUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where userUuid is not null
        defaultShiftHoraireShouldBeFound("userUuid.specified=true");

        // Get all the shiftHoraireList where userUuid is null
        defaultShiftHoraireShouldNotBeFound("userUuid.specified=false");
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeStartIsEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeStart equals to DEFAULT_TIME_START
        defaultShiftHoraireShouldBeFound("timeStart.equals=" + DEFAULT_TIME_START);

        // Get all the shiftHoraireList where timeStart equals to UPDATED_TIME_START
        defaultShiftHoraireShouldNotBeFound("timeStart.equals=" + UPDATED_TIME_START);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeStart not equals to DEFAULT_TIME_START
        defaultShiftHoraireShouldNotBeFound("timeStart.notEquals=" + DEFAULT_TIME_START);

        // Get all the shiftHoraireList where timeStart not equals to UPDATED_TIME_START
        defaultShiftHoraireShouldBeFound("timeStart.notEquals=" + UPDATED_TIME_START);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeStartIsInShouldWork() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeStart in DEFAULT_TIME_START or UPDATED_TIME_START
        defaultShiftHoraireShouldBeFound("timeStart.in=" + DEFAULT_TIME_START + "," + UPDATED_TIME_START);

        // Get all the shiftHoraireList where timeStart equals to UPDATED_TIME_START
        defaultShiftHoraireShouldNotBeFound("timeStart.in=" + UPDATED_TIME_START);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeStart is not null
        defaultShiftHoraireShouldBeFound("timeStart.specified=true");

        // Get all the shiftHoraireList where timeStart is null
        defaultShiftHoraireShouldNotBeFound("timeStart.specified=false");
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeStart is greater than or equal to DEFAULT_TIME_START
        defaultShiftHoraireShouldBeFound("timeStart.greaterThanOrEqual=" + DEFAULT_TIME_START);

        // Get all the shiftHoraireList where timeStart is greater than or equal to UPDATED_TIME_START
        defaultShiftHoraireShouldNotBeFound("timeStart.greaterThanOrEqual=" + UPDATED_TIME_START);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeStart is less than or equal to DEFAULT_TIME_START
        defaultShiftHoraireShouldBeFound("timeStart.lessThanOrEqual=" + DEFAULT_TIME_START);

        // Get all the shiftHoraireList where timeStart is less than or equal to SMALLER_TIME_START
        defaultShiftHoraireShouldNotBeFound("timeStart.lessThanOrEqual=" + SMALLER_TIME_START);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeStartIsLessThanSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeStart is less than DEFAULT_TIME_START
        defaultShiftHoraireShouldNotBeFound("timeStart.lessThan=" + DEFAULT_TIME_START);

        // Get all the shiftHoraireList where timeStart is less than UPDATED_TIME_START
        defaultShiftHoraireShouldBeFound("timeStart.lessThan=" + UPDATED_TIME_START);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeStart is greater than DEFAULT_TIME_START
        defaultShiftHoraireShouldNotBeFound("timeStart.greaterThan=" + DEFAULT_TIME_START);

        // Get all the shiftHoraireList where timeStart is greater than SMALLER_TIME_START
        defaultShiftHoraireShouldBeFound("timeStart.greaterThan=" + SMALLER_TIME_START);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeEndIsEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeEnd equals to DEFAULT_TIME_END
        defaultShiftHoraireShouldBeFound("timeEnd.equals=" + DEFAULT_TIME_END);

        // Get all the shiftHoraireList where timeEnd equals to UPDATED_TIME_END
        defaultShiftHoraireShouldNotBeFound("timeEnd.equals=" + UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeEnd not equals to DEFAULT_TIME_END
        defaultShiftHoraireShouldNotBeFound("timeEnd.notEquals=" + DEFAULT_TIME_END);

        // Get all the shiftHoraireList where timeEnd not equals to UPDATED_TIME_END
        defaultShiftHoraireShouldBeFound("timeEnd.notEquals=" + UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeEndIsInShouldWork() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeEnd in DEFAULT_TIME_END or UPDATED_TIME_END
        defaultShiftHoraireShouldBeFound("timeEnd.in=" + DEFAULT_TIME_END + "," + UPDATED_TIME_END);

        // Get all the shiftHoraireList where timeEnd equals to UPDATED_TIME_END
        defaultShiftHoraireShouldNotBeFound("timeEnd.in=" + UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeEnd is not null
        defaultShiftHoraireShouldBeFound("timeEnd.specified=true");

        // Get all the shiftHoraireList where timeEnd is null
        defaultShiftHoraireShouldNotBeFound("timeEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeEnd is greater than or equal to DEFAULT_TIME_END
        defaultShiftHoraireShouldBeFound("timeEnd.greaterThanOrEqual=" + DEFAULT_TIME_END);

        // Get all the shiftHoraireList where timeEnd is greater than or equal to UPDATED_TIME_END
        defaultShiftHoraireShouldNotBeFound("timeEnd.greaterThanOrEqual=" + UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeEnd is less than or equal to DEFAULT_TIME_END
        defaultShiftHoraireShouldBeFound("timeEnd.lessThanOrEqual=" + DEFAULT_TIME_END);

        // Get all the shiftHoraireList where timeEnd is less than or equal to SMALLER_TIME_END
        defaultShiftHoraireShouldNotBeFound("timeEnd.lessThanOrEqual=" + SMALLER_TIME_END);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeEndIsLessThanSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeEnd is less than DEFAULT_TIME_END
        defaultShiftHoraireShouldNotBeFound("timeEnd.lessThan=" + DEFAULT_TIME_END);

        // Get all the shiftHoraireList where timeEnd is less than UPDATED_TIME_END
        defaultShiftHoraireShouldBeFound("timeEnd.lessThan=" + UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByTimeEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        // Get all the shiftHoraireList where timeEnd is greater than DEFAULT_TIME_END
        defaultShiftHoraireShouldNotBeFound("timeEnd.greaterThan=" + DEFAULT_TIME_END);

        // Get all the shiftHoraireList where timeEnd is greater than SMALLER_TIME_END
        defaultShiftHoraireShouldBeFound("timeEnd.greaterThan=" + SMALLER_TIME_END);
    }

    @Test
    @Transactional
    void getAllShiftHorairesByAppointementsIsEqualToSomething() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);
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
        shiftHoraire.addAppointements(appointements);
        shiftHoraireRepository.saveAndFlush(shiftHoraire);
        Long appointementsId = appointements.getId();

        // Get all the shiftHoraireList where appointements equals to appointementsId
        defaultShiftHoraireShouldBeFound("appointementsId.equals=" + appointementsId);

        // Get all the shiftHoraireList where appointements equals to (appointementsId + 1)
        defaultShiftHoraireShouldNotBeFound("appointementsId.equals=" + (appointementsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShiftHoraireShouldBeFound(String filter) throws Exception {
        restShiftHoraireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shiftHoraire.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].timeStart").value(hasItem(sameInstant(DEFAULT_TIME_START))))
            .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(sameInstant(DEFAULT_TIME_END))));

        // Check, that the count call also returns 1
        restShiftHoraireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShiftHoraireShouldNotBeFound(String filter) throws Exception {
        restShiftHoraireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShiftHoraireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShiftHoraire() throws Exception {
        // Get the shiftHoraire
        restShiftHoraireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShiftHoraire() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();

        // Update the shiftHoraire
        ShiftHoraire updatedShiftHoraire = shiftHoraireRepository.findById(shiftHoraire.getId()).get();
        // Disconnect from session so that the updates on updatedShiftHoraire are not directly saved in db
        em.detach(updatedShiftHoraire);
        updatedShiftHoraire.userUuid(UPDATED_USER_UUID).timeStart(UPDATED_TIME_START).timeEnd(UPDATED_TIME_END);
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(updatedShiftHoraire);

        restShiftHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shiftHoraireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
        ShiftHoraire testShiftHoraire = shiftHoraireList.get(shiftHoraireList.size() - 1);
        assertThat(testShiftHoraire.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testShiftHoraire.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testShiftHoraire.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void putNonExistingShiftHoraire() throws Exception {
        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();
        shiftHoraire.setId(count.incrementAndGet());

        // Create the ShiftHoraire
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShiftHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shiftHoraireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShiftHoraire() throws Exception {
        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();
        shiftHoraire.setId(count.incrementAndGet());

        // Create the ShiftHoraire
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShiftHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShiftHoraire() throws Exception {
        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();
        shiftHoraire.setId(count.incrementAndGet());

        // Create the ShiftHoraire
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShiftHoraireMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShiftHoraireWithPatch() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();

        // Update the shiftHoraire using partial update
        ShiftHoraire partialUpdatedShiftHoraire = new ShiftHoraire();
        partialUpdatedShiftHoraire.setId(shiftHoraire.getId());

        partialUpdatedShiftHoraire.userUuid(UPDATED_USER_UUID).timeStart(UPDATED_TIME_START).timeEnd(UPDATED_TIME_END);

        restShiftHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShiftHoraire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShiftHoraire))
            )
            .andExpect(status().isOk());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
        ShiftHoraire testShiftHoraire = shiftHoraireList.get(shiftHoraireList.size() - 1);
        assertThat(testShiftHoraire.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testShiftHoraire.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testShiftHoraire.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void fullUpdateShiftHoraireWithPatch() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();

        // Update the shiftHoraire using partial update
        ShiftHoraire partialUpdatedShiftHoraire = new ShiftHoraire();
        partialUpdatedShiftHoraire.setId(shiftHoraire.getId());

        partialUpdatedShiftHoraire.userUuid(UPDATED_USER_UUID).timeStart(UPDATED_TIME_START).timeEnd(UPDATED_TIME_END);

        restShiftHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShiftHoraire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShiftHoraire))
            )
            .andExpect(status().isOk());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
        ShiftHoraire testShiftHoraire = shiftHoraireList.get(shiftHoraireList.size() - 1);
        assertThat(testShiftHoraire.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testShiftHoraire.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testShiftHoraire.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
    }

    @Test
    @Transactional
    void patchNonExistingShiftHoraire() throws Exception {
        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();
        shiftHoraire.setId(count.incrementAndGet());

        // Create the ShiftHoraire
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShiftHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shiftHoraireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShiftHoraire() throws Exception {
        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();
        shiftHoraire.setId(count.incrementAndGet());

        // Create the ShiftHoraire
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShiftHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShiftHoraire() throws Exception {
        int databaseSizeBeforeUpdate = shiftHoraireRepository.findAll().size();
        shiftHoraire.setId(count.incrementAndGet());

        // Create the ShiftHoraire
        ShiftHoraireDTO shiftHoraireDTO = shiftHoraireMapper.toDto(shiftHoraire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShiftHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shiftHoraireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShiftHoraire in the database
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShiftHoraire() throws Exception {
        // Initialize the database
        shiftHoraireRepository.saveAndFlush(shiftHoraire);

        int databaseSizeBeforeDelete = shiftHoraireRepository.findAll().size();

        // Delete the shiftHoraire
        restShiftHoraireMockMvc
            .perform(delete(ENTITY_API_URL_ID, shiftHoraire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShiftHoraire> shiftHoraireList = shiftHoraireRepository.findAll();
        assertThat(shiftHoraireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
