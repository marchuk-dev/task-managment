package com.miratech.miratechtechtask.service.impl;

import com.miratech.miratechtechtask.dto.TaskDto;
import com.miratech.miratechtechtask.dto.TaskStatus;
import com.miratech.miratechtechtask.entity.Task;
import com.miratech.miratechtechtask.mapper.TaskMapper;
import com.miratech.miratechtechtask.repository.TaskRepository;
import com.miratech.miratechtechtask.service.TaskManagementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaskManagementServiceImpl implements TaskManagementService {

    public static final String FORMAT_TASK_NOT_FOUND = "Task with id %d not found";

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    /**
     * Retrieves all tasks based on optional filtering parameters and paginates the results.
     *
     * @param pageable Pagination information.
     * @param title    (Optional) Title to filter tasks by.
     * @param status   (Optional) Status to filter tasks by.
     * @return A Page containing TaskDto objects representing the tasks.
     */
    @Override
    public Page<TaskDto> getAll(Pageable pageable, String title, String status) {
        Specification<Task> initialSpec = null;
        if (Objects.nonNull(title)) {
            initialSpec = Specification.where((root, query, cb) -> cb.equal(root.get("title"), title));
        }
        if (Objects.nonNull(status)) {
            TaskStatus taskStatus = TaskStatus.fromStatus(status);
            initialSpec = Objects.isNull(initialSpec) ?
                    Specification.where((root, query, cb) -> cb.equal(root.get("status"), taskStatus)) :
                    initialSpec.and((root, query, cb) -> cb.equal(root.get("status"), taskStatus));
        }
        Page<Task> tasks = Objects.isNull(initialSpec) ?
                taskRepository.findAll(pageable) :
                taskRepository.findAll(initialSpec, pageable);
        return tasks.map(taskMapper::toDto);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The TaskDto object representing the task with the specified ID.
     * @throws EntityNotFoundException If no task with the specified ID is found.
     */
    @Override
    public TaskDto getById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FORMAT_TASK_NOT_FOUND, id)));
    }

    /**
     * Creates a new task.
     *
     * @param dto The TaskDto object representing the task to create.
     * @return The TaskDto object representing the newly created task.
     */
    @Override
    public TaskDto create(TaskDto dto) {
        return taskMapper.toDto(taskRepository.save(taskMapper.toEntity(dto)));
    }

    /**
     * Updates an existing task.
     *
     * @param dto The TaskDto object representing the updated task data.
     * @return The TaskDto object representing the updated task.
     * @throws EntityNotFoundException If no task with the specified ID is found.
     */
    @Override
    public TaskDto update(Long id, TaskDto dto) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(dto.title());
                    existingTask.setDescription(dto.description());
                    existingTask.setStatus(TaskStatus.fromStatus(dto.status()));
                    return taskRepository.save(existingTask);
                })
                .map(taskMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FORMAT_TASK_NOT_FOUND, id)));
    }

    /**
     * Updates the status of an existing task.
     *
     * @param id     The ID of the task to update.
     * @param status The new status for the task.
     * @return The TaskDto object representing the updated task.
     * @throws EntityNotFoundException If no task with the specified ID is found.
     */
    @Override
    public TaskDto updateStatus(Long id, TaskStatus status) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setStatus(status);
                    return task;
                })
                .map(taskRepository::save)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FORMAT_TASK_NOT_FOUND, id)));
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @throws EntityNotFoundException If no task with the specified ID is found.
     */
    @Override
    public void deleteById(Long id) {
        taskRepository.findById(id)
                .ifPresentOrElse(
                        taskRepository::delete,
                        () -> {
                            throw new EntityNotFoundException(String.format(FORMAT_TASK_NOT_FOUND, id));
                        }
                );
    }

}
