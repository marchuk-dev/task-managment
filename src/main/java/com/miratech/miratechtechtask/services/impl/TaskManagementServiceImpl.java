package com.miratech.miratechtechtask.services.impl;

import com.miratech.miratechtechtask.dto.TaskDto;
import com.miratech.miratechtechtask.dto.TaskStatus;
import com.miratech.miratechtechtask.entities.Task;
import com.miratech.miratechtechtask.mappers.TaskMapper;
import com.miratech.miratechtechtask.repositories.TaskRepository;
import com.miratech.miratechtechtask.services.TaskManagementService;
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

    @Override
    public TaskDto getById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FORMAT_TASK_NOT_FOUND, id)));
    }

    @Override
    public TaskDto create(TaskDto dto) {
        return taskMapper.toDto(taskRepository.save(taskMapper.toEntity(dto)));
    }

    @Override
    public TaskDto update(Long id, TaskDto dto) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(dto.getTitle());
                    existingTask.setDescription(dto.getDescription());
                    existingTask.setStatus(TaskStatus.fromStatus(dto.getStatus()));
                    return taskRepository.save(existingTask);
                })
                .map(taskMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FORMAT_TASK_NOT_FOUND, id)));
    }

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
