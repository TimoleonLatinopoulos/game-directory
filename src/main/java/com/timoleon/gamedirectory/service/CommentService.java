package com.timoleon.gamedirectory.service;

import com.timoleon.gamedirectory.domain.Comment;
import com.timoleon.gamedirectory.domain.User;
import com.timoleon.gamedirectory.repository.CommentRepository;
import com.timoleon.gamedirectory.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comment}.
 */
@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    /**
     * Save a comment.
     *
     * @param comment the entity to save.
     * @return the persisted entity.
     */
    public Comment save(Comment comment) {
        log.debug("Request to save Comment : {}", comment);
        setCommentDetails(comment, false);

        return commentRepository.save(comment);
    }

    /**
     * Update a comment.
     *
     * @param comment the entity to save.
     * @return the persisted entity.
     */
    public Comment update(Comment comment) {
        log.debug("Request to update Comment : {}", comment);
        setCommentDetails(comment, true);

        return commentRepository.save(comment);
    }

    private Comment setCommentDetails(Comment comment, boolean isUpdate) {
        User currentUser = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("User not found", "userGame", "userNotFound"));

        if (!isUpdate) {
            List<Comment> previousComments = commentRepository.findByGameAndUser(comment.getGame().getId(), currentUser.getId());
            if (previousComments.size() > 0) {
                throw new BadRequestAlertException("Comment for user already exists", "userGame", "alreadyExists");
            }
        }

        comment.setUser(currentUser);
        comment.setDatePosted(LocalDate.now());
        comment.setDescription(comment.getDescription().replace("\n", "<br>").replace("\r", "<br>"));

        return comment;
    }

    /**
     * Partially update a comment.
     *
     * @param comment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Comment> partialUpdate(Comment comment) {
        log.debug("Request to partially update Comment : {}", comment);

        return commentRepository
            .findById(comment.getId())
            .map(existingComment -> {
                if (comment.getDescription() != null) {
                    existingComment.setDescription(comment.getDescription());
                }
                if (comment.getRecommended() != null) {
                    existingComment.setRecommended(comment.getRecommended());
                }
                if (comment.getDatePosted() != null) {
                    existingComment.setDatePosted(comment.getDatePosted());
                }

                return existingComment;
            })
            .map(commentRepository::save);
    }

    /**
     * Get all the comments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        log.debug("Request to get all Comments");
        return commentRepository.findAll();
    }

    /**
     * Get all the comments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Comment> findAllWithEagerRelationships(Pageable pageable) {
        return commentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one comment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Comment> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Get all the comments of a game without current user.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Comment> findByGameAndNotCurrentUser(Long id) {
        log.debug("Request to get all Comments of a game without current user");

        User currentUser = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("User not found", "userGame", "userNotFound"));

        return commentRepository.findByGameAndNotUser(id, currentUser.getId());
    }

    /**
     * Get the comment of a game for the current user.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Comment> findByGameAndCurrentUser(Long id) {
        log.debug("Request to get comment of a game for the current user");

        User currentUser = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("User not found", "userGame", "userNotFound"));

        return commentRepository.findByGameAndUser(id, currentUser.getId());
    }

    /**
     * Delete the comment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
    }
}
