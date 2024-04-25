package com.miratech.miratechtechtask.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miratech.miratechtechtask.dto.TaskDto;
import com.miratech.miratechtechtask.dto.TaskStatus;
import com.miratech.miratechtechtask.dto.UpdateStatusDto;
import com.miratech.miratechtechtask.entities.Task;
import com.miratech.miratechtechtask.repositories.TaskRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.util.RandomStringUtils;
import lombok.SneakyThrows;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FlywayTest
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@ActiveProfiles(profiles = {"test"})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("Task Controller API tests")
public class TaskControllerTest {
    private static final String URL = "/miratech/tasks";
    public static final String MESSAGE_NOT_FOUND = "Task with id %d not found";
    public static final String STATUS_NOT_FOUND = "NOT_FOUND";
    public static final String PENDING = "pending";
    public static final Integer ID = 100;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @BeforeAll
    public void setup() {
        taskRepository.deleteAll();
        initData();

        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    private void initData() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tasks.add(Task.builder()
                    .title("Title " + RandomStringUtils.randomAlphabetic(5))
                    .description("Description " + RandomStringUtils.randomAlphabetic(10))
                    .status(TaskStatus.values()[RandomUtils.nextInt(0, TaskStatus.values().length)])
                    .build());
        }
        taskRepository.saveAll(tasks);
    }

    @Nested
    @DisplayName("GET all tasks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GetAll {

        private List<Task> tasks;

        @BeforeAll
        void beforeAll() {
            tasks = taskRepository.findAll().stream()
                    .sorted(Comparator.comparing(Task::getTitle))
                    .toList();
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] 100 init tasks")
        void testGetIs200() {
            mvc.perform(get(URL))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.totalElements").value(100))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.sort.sorted").value("true"))
                    .andExpect(jsonPath("$.totalPages").value(10))
                    .andExpect(jsonPath("$.content.[0].title", is(tasks.get(0).getTitle())))
                    .andExpect(jsonPath("$.content.[9].status", is(tasks.get(9).getStatus().getStatus())));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] offset 50")
        void testGet() {
            mvc.perform(get(URL)
                            .param("offset", "50"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.totalElements").value(100))
                    .andExpect(jsonPath("$.size").value(50))
                    .andExpect(jsonPath("$.totalPages").value(2))
                    .andExpect(jsonPath("$.sort.sorted").value("true"))
                    .andExpect(jsonPath("$.content.[0].title", is(tasks.get(0).getTitle())))
                    .andExpect(jsonPath("$.content.[9].status", is(tasks.get(9).getStatus().getStatus())));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] get elements from middle page")
        void testGetMiddleIs200() {
            int pagesAmount = (tasks.size() / 10) + 1;
            int middlePage = pagesAmount / 2;
            mvc.perform(get(URL)
                            .param("page", String.valueOf(middlePage)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.totalElements").value(tasks.size()))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.sort.sorted").value("true"))
                    .andExpect(jsonPath("$.totalPages").value(pagesAmount - 1))
                    .andExpect(jsonPath("$.number").value(middlePage))
                    .andExpect(jsonPath("$.content.[0].title", is(tasks.get(tasks.size() / 2).getTitle())))
                    .andExpect(jsonPath("$.content.[9].status", is(tasks.get(tasks.size() / 2 + 9).getStatus().getStatus())));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] search by title")
        void testGetByTitleIs200() {
            Task task = tasks.get(10);
            mvc.perform(get(URL)
                            .param("title", task.getTitle()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.sort.sorted").value("true"))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.number").value(0))
                    .andExpect(jsonPath("$.content.[0].title", is(task.getTitle())))
                    .andExpect(jsonPath("$.content.[0].status", is(task.getStatus().getStatus())));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] search by status")
        void testGetByStatusIs200() {
            Task task = tasks.get(10);
            List<Task> statusTasks = tasks.stream()
                    .filter(t -> t.getStatus().equals(task.getStatus()))
                    .sorted(Comparator.comparing(Task::getTitle))
                    .toList();
            mvc.perform(get(URL)
                            .param("status", task.getStatus().getStatus()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.totalElements").value(statusTasks.size()))
                    .andExpect(jsonPath("$.sort.sorted").value("true"))
                    .andExpect(jsonPath("$.number").value(0))
                    .andExpect(jsonPath("$.content.[0].title", is(statusTasks.get(0).getTitle())))
                    .andExpect(jsonPath("$.content.[0].status", is(statusTasks.get(0).getStatus().getStatus())));
        }

    }

    @Nested
    @DisplayName("GET task by ID")
    class GetById {

        @Test
        @SneakyThrows
        @DisplayName("[200] GET by ID")
        void testGetByIdIs200() {
            Task task = taskRepository.findById(ID.longValue()).orElseThrow();
            mvc.perform(get(URL + "/" + task.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value(task.getId()))
                    .andExpect(jsonPath("$.title", is(task.getTitle())))
                    .andExpect(jsonPath("$.status", is(task.getStatus().getStatus())));
        }

        @Test
        @SneakyThrows
        @DisplayName("[404] not found by ID")
        void testGetByIdIs404() {
            mvc.perform(get(URL + "/1000"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(String.format(MESSAGE_NOT_FOUND, 1000)))
                    .andExpect(jsonPath("$.status").value(STATUS_NOT_FOUND))
                    .andDo(print());
        }

    }


    @Nested
    @DisplayName("POST create")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Create {

        private TaskDto dto;

        @BeforeAll
        void beforeAll() {
            dto = TaskDto.builder()
                    .title("Test")
                    .description("Testing")
                    .status(TaskStatus.IN_TESTING.getStatus())
                    .build();
        }

        @AfterAll
        void afterAll() {
            taskRepository.deleteAll(taskRepository.findByTitle(dto.getTitle()));
        }


        @Test
        @SneakyThrows
        @DisplayName("[201] task created")
        void testPostNewTaskIs201() {
            mvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(dto))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("Test"))
                    .andExpect(jsonPath("$.description").value("Testing"))
                    .andExpect(jsonPath("$.status").value(TaskStatus.IN_TESTING.getStatus()));
        }


    }

    @Nested
    @DisplayName("PATCH update")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class UpdateStatus {

        private Task task;
        private UpdateStatusDto statusDto;

        @BeforeAll
        void beforeAll() {
            statusDto = UpdateStatusDto.builder().status(PENDING).build();
            task = taskRepository.save(Task.builder()
                    .title("Test")
                    .description("Testing")
                    .status(TaskStatus.IN_TESTING)
                    .build()
            );
        }

        @AfterAll
        void afterAll() {
            taskRepository.findById(task.getId()).ifPresent(taskRepository::delete);
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] update whole task by ID")
        void testPutIs200() {
            mvc.perform(patch(URL + "/" + task.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(statusDto))
                    )
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value(task.getId()))
                    .andExpect(jsonPath("$.title").value(task.getTitle()))
                    .andExpect(jsonPath("$.description").value(task.getDescription()))
                    .andExpect(jsonPath("$.status").value(statusDto.status()))

            ;
        }

        @Test
        @SneakyThrows
        @DisplayName("[404] not found by ID")
        void testPutNotFoundIs404() {
            mvc.perform(patch(URL + "/" + "500")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(statusDto))
                    )
                    .andExpect(status().isNotFound())
                    .andDo(print())
                    .andExpect(jsonPath("$.message").value(String.format(MESSAGE_NOT_FOUND, 500)))
                    .andExpect(jsonPath("$.status").value(STATUS_NOT_FOUND))
            ;
        }

    }

    @Nested
    @DisplayName("PUT update")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Update {
        private Task task;
        private TaskDto requestObject;

        @BeforeAll
        void beforeAll() {
            requestObject = TaskDto.builder()
                    .title("Test")
                    .description("Testing")
                    .status(TaskStatus.IN_TESTING.getStatus())
                    .build();
            task = taskRepository.save(Task.builder()
                    .title("Test")
                    .description("Testing")
                    .status(TaskStatus.IN_TESTING)
                    .build()
            );
        }

        @AfterAll
        void afterAll() {
            taskRepository.findById(task.getId()).ifPresent(taskRepository::delete);
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] update whole task by ID")
        void testPutIs200() {
            mvc.perform(put(URL + "/" + task.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestObject))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(task.getId()))
                    .andExpect(jsonPath("$.title").value(requestObject.getTitle()))
                    .andExpect(jsonPath("$.description").value(requestObject.getDescription()))
                    .andExpect(jsonPath("$.status").value(requestObject.getStatus()));
        }

        @Test
        @SneakyThrows
        @DisplayName("[404] not found by id")
        void testPutIs404() {
            mvc.perform(put(URL + "/500")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestObject))
                    )
                    .andExpect(jsonPath("$.message").value(String.format(MESSAGE_NOT_FOUND, 500)))
                    .andExpect(jsonPath("$.status").value(STATUS_NOT_FOUND))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("DELETE task")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Delete {

        private Task task;

        @BeforeAll
        void beforeAll() {
            task = taskRepository.save(Task.builder()
                    .title("Test")
                    .description("Testing")
                    .status(TaskStatus.IN_TESTING)
                    .build()
            );
        }

        @AfterAll
        void afterAll() {
            taskRepository.findById(task.getId()).ifPresent(taskRepository::delete);
        }

        @Test
        @SneakyThrows
        @DisplayName("[404] task by id not found")
        @Order(1)
        void testDeleteByIdWhenNoFoundIs404() {
            mvc.perform(delete(URL + "/1000"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(String.format(MESSAGE_NOT_FOUND, 1000)))
                    .andExpect(jsonPath("$.status").value(STATUS_NOT_FOUND));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] task by id deleted")
        @Order(5)
        void testDeleteByIdIs200() {
            mvc.perform(delete(URL + "/" + task.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
            ;
        }

        @Test
        @SneakyThrows
        @DisplayName("[404] already deleted")
        @Order(10)
        void testAlreadyDeletedByIdIs200() {
            mvc.perform(delete(URL + "/" + task.getId()))
                    .andDo(print())
                    .andExpect(status().isNotFound())
            ;
        }

    }
}
