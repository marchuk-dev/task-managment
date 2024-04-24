package com.miratech.miratechtechtask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miratech.miratechtechtask.entity.Task;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link Task}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskDto(Long id, @NotBlank String title, @NotBlank String description, TaskStatus status) implements Serializable {
}