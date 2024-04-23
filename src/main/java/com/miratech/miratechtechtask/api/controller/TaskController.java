package com.miratech.miratechtechtask.api.controller;

import com.miratech.miratechtechtask.api.TaskApi;
import com.miratech.miratechtechtask.service.TaskManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Simple CRUD RESTful API for managing tasks
 */
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("miratech")
public class TaskController implements TaskApi {

    private final TaskManagementService taskService;

    @GetMapping("tasks")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
                taskService.receiveAll());
    }

    @GetMapping("tasks/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(
                taskService.receiveById());
    }

    @PostMapping("tasks")
    public ResponseEntity<?> publish() {
        return ResponseEntity.ok(
                taskService.post());
    }

    @PutMapping("tasks/{id}")
    public ResponseEntity<?> updateById(@PathVariable Integer id) {
        return ResponseEntity.ok(
                taskService.updateById());
    }

    @DeleteMapping("tasks/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        return ResponseEntity.ok(
                taskService.deleteById());
    }
}
