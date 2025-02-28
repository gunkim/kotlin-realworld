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

The project is structured using a modular architecture, where each module plays a specific role:

- **/api**: Contains the main application server code, including the entry point. This module implements the
  presentation and application layers.
- **/core**: Houses the domain layer, defining the core business logic and domain rules.
- **/core-impl**: Provides concrete implementations for the domain logic specified in the `/core` module. This includes
  functionality such as CRUD operations and integration with specific frameworks.
- **/e2e**: Maintains end-to-end test scripts and related resources.

![](/docs/module-structures.png)

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