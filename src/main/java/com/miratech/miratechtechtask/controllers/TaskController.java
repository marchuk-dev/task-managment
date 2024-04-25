package com.miratech.miratechtechtask.controllers;

import com.miratech.miratechtechtask.dto.TaskDto;
import com.miratech.miratechtechtask.dto.TaskStatus;
import com.miratech.miratechtechtask.dto.UpdateStatusDto;
import com.miratech.miratechtechtask.services.TaskManagementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Simple CRUD RESTful API for managing tasks
 */
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("miratech")
@Validated
public class TaskController {

    private final TaskManagementService taskService;

    /**
     * Retrieves all tasks.
     *
     * @return A list of TaskDto objects representing all tasks.
     */
    @ApiOperation("Get all tasks")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Return all present tasks")
    })
    @GetMapping("tasks")
    public Page<TaskDto> getAll(@Min(0) @RequestParam(value = "page", required = false, defaultValue = "0") @Valid Integer page,
                                @Min(1) @RequestParam(value = "offset", required = false, defaultValue = "10") @Valid Integer offset,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "status", required = false) String status) {
        Pageable pageable = PageRequest.of(page, offset,
                Sort.by("title"));
        return taskService.getAll(pageable, title, status);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The TaskDto object representing the task with the specified ID.
     */
    @ApiOperation("Receive task by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Task is returned"),
            @ApiResponse(code = 404, message = "Task with the specified ID not found")
    })
    @GetMapping("tasks/{id}")
    public TaskDto getById(@PathVariable("id") Long id) {
        return taskService.getById(id);
    }

    /**
     * Create a new task.
     *
     * @param dto The TaskDto containing the details of the task to be created.
     * @return TaskDto containing the created task if successful, with HTTP status code 201 (Created).
     */
    @ApiOperation("Create a new task")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Task created successfully")
    })
    @PostMapping("tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody @Valid TaskDto dto) {
        return taskService.create(dto);
    }

    /**
     * Updates an existing task by its ID.
     *
     * @param id  The ID of the task to update.
     * @param dto The TaskDto object representing the updated task data.
     * @return The TaskDto object representing the updated task.
     */
    @ApiOperation("Update task by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Task updated successfully"),
            @ApiResponse(code = 404, message = "Task with the specified ID not found")
    })
    @PutMapping("tasks/{id}")
    public TaskDto updateById(@PathVariable("id") Long id, @RequestBody @Valid TaskDto dto) {
        return taskService.update(id, dto);
    }

    /**
     * Update the status of a task by its ID.
     *
     * @param id        The ID of the task to update.
     * @param statusDto The new status of the task.
     * @return The updated TaskDto representing the task with the new status.
     */
    @ApiOperation("Update task status by ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Task status updated successfully"),
            @ApiResponse(code = 404, message = "Task with the specified ID not found")
    })
    @PatchMapping("tasks/{id}")
    public TaskDto updateStatus(@PathVariable("id") Long id, @RequestBody @Valid UpdateStatusDto statusDto) {
        return taskService.updateStatus(id, TaskStatus.fromStatus(statusDto.status()));
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     */
    @ApiOperation("Delete task by ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Task deleted successfully"),
            @ApiResponse(code = 404, message = "Task with the specified ID not found")
    })
    @DeleteMapping("tasks/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        taskService.deleteById(id);
    }

}
