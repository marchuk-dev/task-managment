package com.miratech.miratechtechtask.service.impl;

import com.miratech.miratechtechtask.repository.TaskRepository;
import com.miratech.miratechtechtask.service.TaskManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;

    @Override
    public Object receiveAll() {
        return null;
    }

    @Override
    public Object receiveById() {
        return null;
    }

    @Override
    public Object post() {
        return null;
    }

    @Override
    public Object updateById() {
        return null;
    }

    @Override
    public Object deleteById() {
        return null;
    }
}
