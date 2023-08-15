package com.timoleon.gamedirectory.web.rest;

import com.timoleon.gamedirectory.domain.Category;
import com.timoleon.gamedirectory.service.CategoryService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.timoleon.gamedirectory.domain.Category}.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        log.debug("REST request to get all Categories");
        return categoryService.findAll();
    }

    /**
     * {@code GET  /categories/used} : get all the categories used in gameDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories/used")
    public List<Category> getAllUsedCategories() {
        log.debug("REST request to get all Categories used in gameDetails");
        return categoryService.findAllUsed();
    }

    /**
     * {@code GET  /categories/search} : search for categories.
     *
     * @param input the input for the search.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories/search")
    public List<Category> searchCategories(@RequestParam String input) {
        log.debug("REST request to search for Category");
        return categoryService.findLike(input);
    }

    /**
     * {@code GET  /categories/:id} : get the "id" category.
     *
     * @param id the id of the category to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the category, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        Optional<Category> category = categoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(category);
    }
}
