package com.miratech.miratechtechtask.services;

import com.miratech.miratechtechtask.dto.TaskDto;
import com.miratech.miratechtechtask.dto.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing tasks.
 */
public interface TaskManagementService {

    /**
     * Retrieves a page of tasks based on the provided criteria.
     *
     * @param pageable Pagination information.
     * @param title    Title of the task to filter by (optional).
     * @param status   Status of the task to filter by (optional).
     * @return A page of TaskDto objects.
     */
    Page<TaskDto> getAll(Pageable pageable, String title, String status);

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id The unique identifier of the task.
     * @return The TaskDto object corresponding to the given ID.
     */
    TaskDto getById(Long id);

    /**
     * Creates a new task.
     *
     * @param dto The TaskDto object representing the new task.
     * @return The created TaskDto object.
     */
    TaskDto create(TaskDto dto);

    /**
     * Updates an existing task.
     *
     * @param id  The unique identifier of the task to be updated.
     * @param dto The TaskDto object representing the updated task details.
     * @return The updated TaskDto object.
     */
    TaskDto update(Long id, TaskDto dto);

    /**
     * Updates the status of a task.
     *
     * @param id     The unique identifier of the task whose status is to be updated.
     * @param status The new status for the task.
     * @return The updated TaskDto object.
     */
    TaskDto updateStatus(Long id, TaskStatus status);

    /**
     * Deletes a task by its unique identifier.
     *
     * @param id The unique identifier of the task to be deleted.
     */
    void deleteById(Long id);
}
