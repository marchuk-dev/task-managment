**This is solution of the technital task from Miratech for Java Trainee from Mykhailo Marchuk**

**Info about endpoints you can read on the page http://localhost:8080/swagger-ui/index.html#/ if you run it locally**

**Test Coverage**
![image](https://github.com/marchuk-engineer/miratech-techtask/assets/112648286/ba36f939-aa3c-40f6-b840-8aeedb99b8ce)


<details lang="java">
<summary>How to start:</summary>

<details lang="java">
<summary>In Intellij Idea:</summary>

1. Clone project.
2. Run test
```
mvn test
```
3. Start server
```
mvn clean install -Dmaven.test.skip=true
```

</details>

<details lang="java">
<summary>Docker:</summary>

```
docker pull mishaakamichael999/miratech-techtask
```

```
docker run -d --name miratech-techtask -p 8080:8080 mishaakamichael999/miratech-techtask
```

</details>


</details>

<details lang="java">
<summary>API</summary>
  GET http://localhost:8080/miratech/tasks
  POST http://localhost:8080/miratech/tasks
  GET http://localhost:8080/miratech/tasks/{id}
  PUT http://localhost:8080/miratech/tasks/{id}
  PATCH http://localhost:8080/miratech/tasks/{id}
  DELETE http://localhost:8080/miratech/tasks/{id}
</details>

<details lang="java">
<summary>Task description:</summary>
Simple RESTful API for managing tasks. The API should allow users to perform CRUD operations (Create, Read, Update, Delete) on tasks.

Technologies:

Use Spring Boot for server-side development.
Use any appropriate database of your choice (e.g., PostgreSQL, SQLite, etc.) for storing tasks.
Use git as a version control system (maybe create a private GitHub repository)


Endpoints:

The API should have endpoints for performing CRUD operations on tasks.

Implement the following endpoints:

GET /tasks: Retrieve all tasks.
GET /tasks/:id: Retrieve a specific task by ID.
POST /tasks: Create a new task.
PUT /tasks/:id: Update an existing task by ID.
DELETE /tasks/:id: Delete a task by ID.

Task Model:

A task should have the following attributes:
id: Unique identifier for the task (can be auto-generated).
title: Title of the task.
description: Description of the task.
status: Status of the task (e.g., "pending", "completed", "in progress", etc.).

Data Persistence:

Persist task data using a database of your choice.
Set up appropriate database schema and models for storing tasks.

Optional tasks:

These are not in any particular order.

Validation:
Implement basic validation for input data.
Ensure that required fields are present when creating or updating tasks.
Validate the input data to ensure it meets the expected format and type.
Error Handling:
Implement error handling for various scenarios (e.g., invalid input, server errors, etc.).
Return appropriate HTTP status codes and error messages.
Testing:
Write basic integration tests to ensure that API endpoints work as expected.
Use a testing framework
Test CRUD operations for tasks, including edge cases.
Filtering:
Add optional filtering of tasks by attributes in GET method
Optional attributes may be present in query in form &attr1=value1&attr2=value2
Only tasks with matching attributes are returned in response

</details>





