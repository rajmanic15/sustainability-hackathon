package education.service;

import education.service.dto.QuestionDTO;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link education.domain.Question}.
 */
public interface QuestionService {

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionDTO save(QuestionDTO questionDTO);

    /**
     * Update a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionDTO update(QuestionDTO questionDTO);

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionDTO> findOne(Long id);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
