package com.miratech.miratechtechtask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miratech.miratechtechtask.entities.Task;
import com.miratech.miratechtechtask.validators.EnumValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link Task}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class TaskDto implements Serializable {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @EnumValidator(enumClazz = TaskStatus.class)
    private String status;

}
