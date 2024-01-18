package com.timoleon.gamedirectory.service;

import com.timoleon.gamedirectory.domain.Category;
import com.timoleon.gamedirectory.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Category}.
 */
@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Save a category.
     *
     * @param category the entity to save.
     * @return the persisted entity.
     */
    public Category save(Category category) {
        log.debug("Request to save Category : {}", category);
        return categoryRepository.save(category);
    }

    /**
     * Update a category.
     *
     * @param category the entity to save.
     * @return the persisted entity.
     */
    public Category update(Category category) {
        log.debug("Request to update Category : {}", category);
        return categoryRepository.save(category);
    }

    /**
     * Partially update a category.
     *
     * @param category the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Category> partialUpdate(Category category) {
        log.debug("Request to partially update Category : {}", category);

        return categoryRepository
            .findById(category.getId())
            .map(existingCategory -> {
                if (category.getDescription() != null) {
                    existingCategory.setDescription(category.getDescription());
                }

                return existingCategory;
            })
            .map(categoryRepository::save);
    }

    /**
     * Get all the categories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll();
    }

    /**
     * Get all the categories used in gameDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Category> findAllUsed() {
        log.debug("Request to get all Categories used in gameDetails");
        return categoryRepository.findAllUsed();
    }

    /**
     * Search for categories.
     *
     * @param input the input for the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Category> findLike(String input) {
        log.debug("Request to search for Categories");
        if (input != null) {
            input = "%" + input + "%";
        }
        return categoryRepository.findAllLike(input);
    }

    /**
     * Search for categories.
     *
     * @param input the input for the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Category> findLikeUsed(String input) {
        log.debug("Request to search for Categories used in GameDetails");
        if (input != null) {
            input = "%" + input + "%";
        }
        return categoryRepository.findLikeUsed(input, PageRequest.of(0, 20));
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Category> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id);
    }

    /**
     * Get one category by description.
     *
     * @param description the description of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Category> findOneByDescription(String description) {
        log.debug("Request to get Category : {}", description);
        return categoryRepository.findByDescription(description);
    }

    /**
     * Delete the category by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.deleteById(id);
    }
}
