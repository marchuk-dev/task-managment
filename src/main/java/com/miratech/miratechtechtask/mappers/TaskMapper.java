package com.miratech.miratechtechtask.mappers;

import com.miratech.miratechtechtask.dto.TaskStatus;
import com.miratech.miratechtechtask.entities.Task;
import com.miratech.miratechtechtask.dto.TaskDto;
import org.mapstruct.*;

/**
 * Mapper interface for converting between Task and TaskDto objects.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {

    /**
     * Converts a TaskDto object to a Task entity.
     *
     * @param taskDto The TaskDto object to be converted.
     * @return The corresponding Task entity.
     */
    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskDto taskDto);

    /**
     * Converts a Task entity to a TaskDto object.
     *
     * @param task The Task entity to be converted.
     * @return The corresponding TaskDto object.
     */
    TaskDto toDto(Task task);

    /**
     * Maps a TaskStatus enum value to a string representation.
     *
     * @param status The TaskStatus enum value to be mapped.
     * @return The string representation of the TaskStatus.
     */
    @ValueMappings({
            @ValueMapping(source = "PENDING", target = "pending"),
            @ValueMapping(source = "COMPLETED", target = "completed"),
            @ValueMapping(source = "IN_PROGRESS", target = "in progress"),
            @ValueMapping(source = "TO_DO", target = "to do"),
            @ValueMapping(source = "TESTING", target = "in testing")
    })
    String mapTaskStatusToString(TaskStatus status);

    /**
     * Maps a string representation of TaskStatus to the corresponding enum value.
     *
     * @param status The string representation of TaskStatus.
     * @return The corresponding TaskStatus enum value.
     */
    @ValueMappings({
            @ValueMapping(source = "pending", target = "PENDING"),
            @ValueMapping(source = "completed", target = "COMPLETED"),
            @ValueMapping(source = "in progress", target = "IN_PROGRESS"),
            @ValueMapping(source = "to do", target = "TO_DO"),
            @ValueMapping(source = "in testing", target = "TESTING")
    })
    TaskStatus mapStringToTaskStatus(String status);
}
