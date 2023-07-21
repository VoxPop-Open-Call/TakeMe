package pt.famility.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
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
import pt.famility.backoffice.IntegrationTest;
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.repository.ChildRepository;
import pt.famility.backoffice.service.criteria.ChildCriteria;
import pt.famility.backoffice.service.dto.ChildDTO;
import pt.famility.backoffice.service.mapper.ChildMapper;

/**
 * Integration tests for the {@link ChildResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChildResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/children";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChildMockMvc;

    private Child child;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createEntity(EntityManager em) {
        Child child = new Child().name(DEFAULT_NAME);
        return child;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createUpdatedEntity(EntityManager em) {
        Child child = new Child().name(UPDATED_NAME);
        return child;
    }

    @BeforeEach
    public void initTest() {
        child = createEntity(em);
    }

    @Test
    @Transactional
    void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();
        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);
        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createChildWithExistingId() throws Exception {
        // Create the Child with an existing ID
        child.setId(1L);
        ChildDTO childDTO = childMapper.toDto(child);

        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = childRepository.findAll().size();
        // set the field null
        child.setName(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isBadRequest());

        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChildren() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList
        restChildMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get the child
        restChildMockMvc
            .perform(get(ENTITY_API_URL_ID, child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(child.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getChildrenByIdFiltering() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        Long id = child.getId();

        defaultChildShouldBeFound("id.equals=" + id);
        defaultChildShouldNotBeFound("id.notEquals=" + id);

        defaultChildShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChildShouldNotBeFound("id.greaterThan=" + id);

        defaultChildShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChildShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChildrenByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList where name equals to DEFAULT_NAME
        defaultChildShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the childList where name equals to UPDATED_NAME
        defaultChildShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChildrenByNameIsInShouldWork() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChildShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the childList where name equals to UPDATED_NAME
        defaultChildShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChildrenByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList where name is not null
        defaultChildShouldBeFound("name.specified=true");

        // Get all the childList where name is null
        defaultChildShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllChildrenByNameContainsSomething() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList where name contains DEFAULT_NAME
        defaultChildShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the childList where name contains UPDATED_NAME
        defaultChildShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChildrenByNameNotContainsSomething() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childList where name does not contain DEFAULT_NAME
        defaultChildShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the childList where name does not contain UPDATED_NAME
        defaultChildShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChildShouldBeFound(String filter) throws Exception {
        restChildMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restChildMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChildShouldNotBeFound(String filter) throws Exception {
        restChildMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChildMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child
        Child updatedChild = childRepository.findById(child.getId()).get();
        // Disconnect from session so that the updates on updatedChild are not directly saved in db
        em.detach(updatedChild);
        updatedChild.name(UPDATED_NAME);
        ChildDTO childDTO = childMapper.toDto(updatedChild);

        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChildWithPatch() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild.name(UPDATED_NAME);

        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChild))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateChildWithPatch() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild.name(UPDATED_NAME);

        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChild))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childList.get(childList.size() - 1);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, childDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChild() throws Exception {
        int databaseSizeBeforeUpdate = childRepository.findAll().size();
        child.setId(count.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(childDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Child in the database
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        int databaseSizeBeforeDelete = childRepository.findAll().size();

        // Delete the child
        restChildMockMvc
            .perform(delete(ENTITY_API_URL_ID, child.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Child> childList = childRepository.findAll();
        assertThat(childList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
