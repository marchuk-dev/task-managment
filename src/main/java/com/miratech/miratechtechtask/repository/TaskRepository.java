package com.miratech.miratechtechtask.repository;

import com.miratech.miratechtechtask.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
