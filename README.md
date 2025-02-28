# ![RealWorld Example App using Kotlin and Spring](logo.png)

> ### Kotlin + Spring Codebase
> A comprehensive codebase featuring real world examples (CRUD, authentication, advanced patterns, etc.) that adheres to
> the [RealWorld](https://github.com/gothinkster/realworld-example-apps) specification and API.

This project was developed to demonstrate a full-stack application built with Kotlin + Spring. It includes robust
features such as CRUD operations, user authentication, routing, pagination, and more. The implementation strictly
follows the Kotlin + Spring community style guides and best practices.

For further information on how this project interacts with various frontends and backends, please refer to
the [RealWorld](https://github.com/gothinkster/realworld) repository.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Features](#features)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [Configuration and Environment Variables](#configuration-and-environment-variables)
- [Running the Application](#running-the-application)
- [Testing](#testing)

---

## Prerequisites

- **JDK 21** or higher

---

## Features

- **CRUD Operations**: Enables create, read, update, and delete functionalities.
- **Authentication & Authorization**: Implements secure user authentication using JWT or similar mechanisms.
- **Routing**: Follows a clean architecture pattern for efficient route management.
- **Pagination & Sorting**: Optimized for handling large datasets.
- **Error Handling**: Centralized error management and logging mechanisms.
- **Testing**: Comprehensive testing includes unit, integration, and end-to-end tests.

---

## Project Structure

This project is built on a solid foundation using Spring Boot 3, JDK 21, Spring MVC, Spring Data JPA, and Spring Security. From the outset, we evaluated various options, including more Kotlin-centric ORMs such as exposed, but ultimately opted for JPA. This decision was driven by the fact that most Kotlin developers also possess strong Java expertise, making familiarity with JPA a practical and essential skill in real-world settings.

By choosing JPA, we ensure that our team can leverage a widely adopted and industry-tested solution, while our decoupled infrastructure remains flexible enough to allow for switching to an alternative ORM in the future if the need arises.

Moreover, Spring remains a dominant framework in the Java ecosystem and offers robust capabilities when used with Kotlin. Consequently, all modules in our project are built upon Spring-related dependencies, reflecting our commitment to utilizing proven technologies and streamlining development efforts.

The project is organized into a modular architecture where each component serves a distinct purpose:

- **/api**: Contains the main application server code including the entry point. This module implements both the presentation and application layers.
- **/core**: Defines the domain layer, encapsulating the core business logic and domain rules.
- **/core-impl**: Provides concrete implementations of the domain logic specified in the `/core` module, including CRUD operations and integration with specific frameworks.
- **/e2e**: Houses the end-to-end test scripts and all related testing resources.

![](/docs/module-structures.png)

---

## **Domain Design**

1. As an experienced Java developer, I am accustomed to the object-oriented paradigm, where internal fields are kept private and each object is responsible for its own behavior. In contrast, functional programming emphasizes immutability, allowing fields to be safely exposed without side effects. Consequently, what might be seen as “anemic objects” or “thin models” in traditional DDD or OOP are not only natural in functional programming, but are often considered best practices.

2. When intentionally designing thin models, the challenge arises as to where to locate the behavior-bearing methods typically associated with object-oriented design. I have chosen to place these methods within a Domain Service in the domain layer to enhance cohesion. This approach is in line with the functional paradigm, which favors a tight coupling between models and functions.

![Domain Design](/docs/domain.png)

---

## **Database Design**

Since there were no strict requirements regarding the field lengths, arbitrary sizes were chosen. To facilitate the use
of UUIDs for domain IDs, the column was defined as `binary(16)`. Additionally, as MySQL InnoDB manages a clustered index
by default, a dedicated primary key using `int` (4 bytes) was selected to prevent unnecessary expansion of the clustered
index. No extra indexes have been defined at this stage in order to avoid premature optimization.

![Schema Diagram](/docs/schema.png)

---

## Running the Application

To start the application, run the following command:

```bash
./gradlew :api:bootRun
```

---

## Testing

### End-to-End Tests

Execute the following commands sequentially to run end-to-end tests:

```bash
./gradlew :api:bootRun
./e2e/run-api-tests.sh
```

### Gradle Tests

Alternatively, run unit and integration tests with:

```bash
./gradlew test
```