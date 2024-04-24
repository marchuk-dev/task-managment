package com.miratech.miratechtechtask.mapper;

import com.miratech.miratechtechtask.entity.Task;
import com.miratech.miratechtechtask.dto.TaskDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);

}
