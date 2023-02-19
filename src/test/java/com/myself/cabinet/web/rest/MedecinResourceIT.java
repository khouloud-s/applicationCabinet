package com.myself.cabinet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myself.cabinet.IntegrationTest;
import com.myself.cabinet.domain.Appointement;
import com.myself.cabinet.domain.Medecin;
import com.myself.cabinet.repository.MedecinRepository;
import com.myself.cabinet.service.criteria.MedecinCriteria;
import com.myself.cabinet.service.dto.MedecinDTO;
import com.myself.cabinet.service.mapper.MedecinMapper;
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
 * Integration tests for the {@link MedecinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedecinResourceIT {

    private static final UUID DEFAULT_USER_UUID = UUID.randomUUID();
    private static final UUID UPDATED_USER_UUID = UUID.randomUUID();

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/medecins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private MedecinMapper medecinMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedecinMockMvc;

    private Medecin medecin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medecin createEntity(EntityManager em) {
        Medecin medecin = new Medecin()
            .userUuid(DEFAULT_USER_UUID)
            .fullName(DEFAULT_FULL_NAME)
            .phone(DEFAULT_PHONE)
            .adress(DEFAULT_ADRESS)
            .isActive(DEFAULT_IS_ACTIVE);
        return medecin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medecin createUpdatedEntity(EntityManager em) {
        Medecin medecin = new Medecin()
            .userUuid(UPDATED_USER_UUID)
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .adress(UPDATED_ADRESS)
            .isActive(UPDATED_IS_ACTIVE);
        return medecin;
    }

    @BeforeEach
    public void initTest() {
        medecin = createEntity(em);
    }

    @Test
    @Transactional
    void createMedecin() throws Exception {
        int databaseSizeBeforeCreate = medecinRepository.findAll().size();
        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);
        restMedecinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isCreated());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeCreate + 1);
        Medecin testMedecin = medecinList.get(medecinList.size() - 1);
        assertThat(testMedecin.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);
        assertThat(testMedecin.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testMedecin.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testMedecin.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testMedecin.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createMedecinWithExistingId() throws Exception {
        // Create the Medecin with an existing ID
        medecin.setId(1L);
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        int databaseSizeBeforeCreate = medecinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedecinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = medecinRepository.findAll().size();
        // set the field null
        medecin.setUserUuid(null);

        // Create the Medecin, which fails.
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        restMedecinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isBadRequest());

        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedecins() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList
        restMedecinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecin.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get the medecin
        restMedecinMockMvc
            .perform(get(ENTITY_API_URL_ID, medecin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medecin.getId().intValue()))
            .andExpect(jsonPath("$.userUuid").value(DEFAULT_USER_UUID.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getMedecinsByIdFiltering() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        Long id = medecin.getId();

        defaultMedecinShouldBeFound("id.equals=" + id);
        defaultMedecinShouldNotBeFound("id.notEquals=" + id);

        defaultMedecinShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMedecinShouldNotBeFound("id.greaterThan=" + id);

        defaultMedecinShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMedecinShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedecinsByUserUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where userUuid equals to DEFAULT_USER_UUID
        defaultMedecinShouldBeFound("userUuid.equals=" + DEFAULT_USER_UUID);

        // Get all the medecinList where userUuid equals to UPDATED_USER_UUID
        defaultMedecinShouldNotBeFound("userUuid.equals=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllMedecinsByUserUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where userUuid not equals to DEFAULT_USER_UUID
        defaultMedecinShouldNotBeFound("userUuid.notEquals=" + DEFAULT_USER_UUID);

        // Get all the medecinList where userUuid not equals to UPDATED_USER_UUID
        defaultMedecinShouldBeFound("userUuid.notEquals=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllMedecinsByUserUuidIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where userUuid in DEFAULT_USER_UUID or UPDATED_USER_UUID
        defaultMedecinShouldBeFound("userUuid.in=" + DEFAULT_USER_UUID + "," + UPDATED_USER_UUID);

        // Get all the medecinList where userUuid equals to UPDATED_USER_UUID
        defaultMedecinShouldNotBeFound("userUuid.in=" + UPDATED_USER_UUID);
    }

    @Test
    @Transactional
    void getAllMedecinsByUserUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where userUuid is not null
        defaultMedecinShouldBeFound("userUuid.specified=true");

        // Get all the medecinList where userUuid is null
        defaultMedecinShouldNotBeFound("userUuid.specified=false");
    }

    @Test
    @Transactional
    void getAllMedecinsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName equals to DEFAULT_FULL_NAME
        defaultMedecinShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the medecinList where fullName equals to UPDATED_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllMedecinsByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName not equals to DEFAULT_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the medecinList where fullName not equals to UPDATED_FULL_NAME
        defaultMedecinShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllMedecinsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultMedecinShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the medecinList where fullName equals to UPDATED_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllMedecinsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName is not null
        defaultMedecinShouldBeFound("fullName.specified=true");

        // Get all the medecinList where fullName is null
        defaultMedecinShouldNotBeFound("fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllMedecinsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName contains DEFAULT_FULL_NAME
        defaultMedecinShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the medecinList where fullName contains UPDATED_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllMedecinsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName does not contain DEFAULT_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the medecinList where fullName does not contain UPDATED_FULL_NAME
        defaultMedecinShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllMedecinsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone equals to DEFAULT_PHONE
        defaultMedecinShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the medecinList where phone equals to UPDATED_PHONE
        defaultMedecinShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMedecinsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone not equals to DEFAULT_PHONE
        defaultMedecinShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the medecinList where phone not equals to UPDATED_PHONE
        defaultMedecinShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMedecinsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultMedecinShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the medecinList where phone equals to UPDATED_PHONE
        defaultMedecinShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMedecinsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone is not null
        defaultMedecinShouldBeFound("phone.specified=true");

        // Get all the medecinList where phone is null
        defaultMedecinShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllMedecinsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone contains DEFAULT_PHONE
        defaultMedecinShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the medecinList where phone contains UPDATED_PHONE
        defaultMedecinShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMedecinsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone does not contain DEFAULT_PHONE
        defaultMedecinShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the medecinList where phone does not contain UPDATED_PHONE
        defaultMedecinShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllMedecinsByAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress equals to DEFAULT_ADRESS
        defaultMedecinShouldBeFound("adress.equals=" + DEFAULT_ADRESS);

        // Get all the medecinList where adress equals to UPDATED_ADRESS
        defaultMedecinShouldNotBeFound("adress.equals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllMedecinsByAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress not equals to DEFAULT_ADRESS
        defaultMedecinShouldNotBeFound("adress.notEquals=" + DEFAULT_ADRESS);

        // Get all the medecinList where adress not equals to UPDATED_ADRESS
        defaultMedecinShouldBeFound("adress.notEquals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllMedecinsByAdressIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress in DEFAULT_ADRESS or UPDATED_ADRESS
        defaultMedecinShouldBeFound("adress.in=" + DEFAULT_ADRESS + "," + UPDATED_ADRESS);

        // Get all the medecinList where adress equals to UPDATED_ADRESS
        defaultMedecinShouldNotBeFound("adress.in=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllMedecinsByAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress is not null
        defaultMedecinShouldBeFound("adress.specified=true");

        // Get all the medecinList where adress is null
        defaultMedecinShouldNotBeFound("adress.specified=false");
    }

    @Test
    @Transactional
    void getAllMedecinsByAdressContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress contains DEFAULT_ADRESS
        defaultMedecinShouldBeFound("adress.contains=" + DEFAULT_ADRESS);

        // Get all the medecinList where adress contains UPDATED_ADRESS
        defaultMedecinShouldNotBeFound("adress.contains=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllMedecinsByAdressNotContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress does not contain DEFAULT_ADRESS
        defaultMedecinShouldNotBeFound("adress.doesNotContain=" + DEFAULT_ADRESS);

        // Get all the medecinList where adress does not contain UPDATED_ADRESS
        defaultMedecinShouldBeFound("adress.doesNotContain=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllMedecinsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where isActive equals to DEFAULT_IS_ACTIVE
        defaultMedecinShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the medecinList where isActive equals to UPDATED_IS_ACTIVE
        defaultMedecinShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMedecinsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultMedecinShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the medecinList where isActive not equals to UPDATED_IS_ACTIVE
        defaultMedecinShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMedecinsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultMedecinShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the medecinList where isActive equals to UPDATED_IS_ACTIVE
        defaultMedecinShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMedecinsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where isActive is not null
        defaultMedecinShouldBeFound("isActive.specified=true");

        // Get all the medecinList where isActive is null
        defaultMedecinShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllMedecinsByAppointementsIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);
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
        medecin.addAppointements(appointements);
        medecinRepository.saveAndFlush(medecin);
        Long appointementsId = appointements.getId();

        // Get all the medecinList where appointements equals to appointementsId
        defaultMedecinShouldBeFound("appointementsId.equals=" + appointementsId);

        // Get all the medecinList where appointements equals to (appointementsId + 1)
        defaultMedecinShouldNotBeFound("appointementsId.equals=" + (appointementsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedecinShouldBeFound(String filter) throws Exception {
        restMedecinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecin.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restMedecinMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedecinShouldNotBeFound(String filter) throws Exception {
        restMedecinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedecinMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedecin() throws Exception {
        // Get the medecin
        restMedecinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();

        // Update the medecin
        Medecin updatedMedecin = medecinRepository.findById(medecin.getId()).get();
        // Disconnect from session so that the updates on updatedMedecin are not directly saved in db
        em.detach(updatedMedecin);
        updatedMedecin
            .userUuid(UPDATED_USER_UUID)
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .adress(UPDATED_ADRESS)
            .isActive(UPDATED_IS_ACTIVE);
        MedecinDTO medecinDTO = medecinMapper.toDto(updatedMedecin);

        restMedecinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medecinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medecinDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
        Medecin testMedecin = medecinList.get(medecinList.size() - 1);
        assertThat(testMedecin.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testMedecin.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testMedecin.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMedecin.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testMedecin.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingMedecin() throws Exception {
        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();
        medecin.setId(count.incrementAndGet());

        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedecinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medecinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medecinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedecin() throws Exception {
        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();
        medecin.setId(count.incrementAndGet());

        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedecinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medecinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedecin() throws Exception {
        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();
        medecin.setId(count.incrementAndGet());

        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedecinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedecinWithPatch() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();

        // Update the medecin using partial update
        Medecin partialUpdatedMedecin = new Medecin();
        partialUpdatedMedecin.setId(medecin.getId());

        partialUpdatedMedecin.fullName(UPDATED_FULL_NAME).phone(UPDATED_PHONE).isActive(UPDATED_IS_ACTIVE);

        restMedecinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedecin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedecin))
            )
            .andExpect(status().isOk());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
        Medecin testMedecin = medecinList.get(medecinList.size() - 1);
        assertThat(testMedecin.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);
        assertThat(testMedecin.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testMedecin.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMedecin.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testMedecin.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateMedecinWithPatch() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();

        // Update the medecin using partial update
        Medecin partialUpdatedMedecin = new Medecin();
        partialUpdatedMedecin.setId(medecin.getId());

        partialUpdatedMedecin
            .userUuid(UPDATED_USER_UUID)
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .adress(UPDATED_ADRESS)
            .isActive(UPDATED_IS_ACTIVE);

        restMedecinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedecin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedecin))
            )
            .andExpect(status().isOk());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
        Medecin testMedecin = medecinList.get(medecinList.size() - 1);
        assertThat(testMedecin.getUserUuid()).isEqualTo(UPDATED_USER_UUID);
        assertThat(testMedecin.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testMedecin.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMedecin.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testMedecin.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingMedecin() throws Exception {
        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();
        medecin.setId(count.incrementAndGet());

        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedecinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medecinDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medecinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedecin() throws Exception {
        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();
        medecin.setId(count.incrementAndGet());

        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedecinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medecinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedecin() throws Exception {
        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();
        medecin.setId(count.incrementAndGet());

        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedecinMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medecinDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        int databaseSizeBeforeDelete = medecinRepository.findAll().size();

        // Delete the medecin
        restMedecinMockMvc
            .perform(delete(ENTITY_API_URL_ID, medecin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
