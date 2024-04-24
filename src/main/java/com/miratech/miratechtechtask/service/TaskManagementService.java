package com.miratech.miratechtechtask.service;

import com.miratech.miratechtechtask.dto.TaskDto;
import com.miratech.miratechtechtask.dto.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskManagementService {
    Page<TaskDto> getAll(Pageable pageable, String title, String status);

    TaskDto getById(Long id);

    TaskDto create(TaskDto dto);

    TaskDto update(Long id, TaskDto dto);

    TaskDto updateStatus(Long id, TaskStatus status);

    void deleteById(Long id);
}
