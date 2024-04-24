package com.miratech.miratechtechtask.mapper;

import com.miratech.miratechtechtask.dto.TaskStatus;
import com.miratech.miratechtechtask.entity.Task;
import com.miratech.miratechtechtask.dto.TaskDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);


    @ValueMappings({
            @ValueMapping(source = "PENDING", target = "pending"),
            @ValueMapping(source = "COMPLETED", target = "completed"),
            @ValueMapping(source = "IN_PROGRESS", target = "in progress"),
            @ValueMapping(source = "TO_DO", target = "to do"),
            @ValueMapping(source = "TESTING", target = "in testing")
    })
    String mapTaskStatusToString(TaskStatus status);

    @ValueMappings({
            @ValueMapping(source = "pending", target = "PENDING"),
            @ValueMapping(source = "completed", target = "COMPLETED"),
            @ValueMapping(source = "in progress", target = "IN_PROGRESS"),
            @ValueMapping(source = "to do", target = "TO_DO"),
            @ValueMapping(source = "in testing", target = "TESTING")
    })
    TaskStatus mapStringToTaskStatus(String status);

}
