# Petstore server - Home Challenge

The challenge is divided into two tasks, API Test Automation and API Performance Testing, applied to the Swagger Petstore.

## Swagger Petstore information
The sample <a href="https://petstore3.swagger.io">Pet Store server</a> allows managing a pet store with CRUD operations. The service has three main resources shown below.

<details>
<summary>please click here to see...</summary>

```
Swagger Petstore API
│
├── Pet (manages the pet catalog)
│   ├── POST /pet             -> Add a new pet
│   ├── GET /pet/{petId}      -> Obtain a pet by Id
│   ├── PUT /pet              -> Update an existing pet
│   ├── DELETE /pet/{petId}   -> Delete a pet by Id
│   ├── GET /pet/findByStatus -> Obtain a pet by Status
│   └── GET /pet/findByTags   -> Obtain a pet by Tags
│
├── Store (handles orders)
│   ├── POST /store/order             -> Create a new order
│   ├── GET /store/order/{orderId}    -> Obtain order by Id
│   ├── DELETE /store/order/{orderId} -> Delete a order by Id
│   └── GET /store/inventory          -> Obtain pets inventory
│
└── User (manages user accounts)
    ├── POST /user                -> Create a new user
    ├── GET /user/{username}      -> Obtain a user by username
    ├── PUT /user/{username}      -> Update an existing user
    ├── DELETE /user/{username}   -> Delete a user
    ├── POST /user/createWithList -> Create multiple users using a listt
    ├── GET /user/login           -> log in to the system
    └── GET /user/logout          -> log out of the system
```

**To run** the server locally, run this task:

```
mvn package jetty:run
```
</details>

> **To confirm** that the server is up and ready for use, a request to the following URL: http://localhost:8080/api/v3/openapi.json must return a `200 OK` response.

## Test Coverage Priorization

The selection of the `/store` service for automation and performance testing was based on its higher business impact and criticality compared to the other services. 

| Service | Priority | Reason | Key test |
|-----|------|------|------|
| `/store` |${\textsf{\color{red}High}}$ | Core business functionality; impacts user experience and data integrity by ensuring that order and inventory data are always synchronized.  | Order creation, order deletion, inventory updates |
| `/pet` |${\textsf{\color{orange}Medium}}$ |Manages pet data; indirectly impacts orders; direct impact in user experience is lower than **/store**|Pet CRUD, validation of pet status|
| `/user` |${\textsf{\color{yellow}Low}}$ |Manages user data; minimal direct impact on core busimess functionality| User CRUD, login & logout|
<br>


> [!NOTE]
> In the hypothetical scenario of automation or performance testing Petstore services, the initial effort should prioritize testing for the `/store` service. Once completed, it can be extended to **/pet** and then **/user** to ensure full coverage of the system.

# 1. API Test Automation

### Built with
This project use the following technologies and tools:

- **IDE:** IntelliJ IDEA
- **Design Pattern:** Screenplay
- **Framework:** Serenity BDD
- **Testing Library:** Rest Assured
- **Behavior-Driven Development (BDD):** Cucumber
- **Build Tool:** Gradle 8.5
- **Programming Language:** Java 21
- **Environment:** Local

These resources ensure a structured, scalable, and easy-to-maintain.

## Structure
<details open>
<summary><h5>The project is structured under Screenplay pattern as follows (please click here to hide...)</h5></summary>

```
PetStore             
└── src
   ├── main
   │   └── java
   │       └── API
   │           └── petStore
   │               ├── interactions
   │               │   ├── checkHealth.java
   │               │   └── sendOrder.java
   │               ├── questions
   │               │   ├── updatedInventory.java
   │               │   └── verifyOrder.java
   │               ├── tasks
   │               │   ├── createOrder.java
   │               │   ├── deleteOrder.java
   │               │   └── getInventory.java
   │               └── utils
   │                   └── obtainIdFromJson.java         
   └── test
       ├── java
       │   └── API
       │       └── petStore
       │           ├── runners
       │           │   └── runner.java
       │           └── stepDefinitions
       │               └── stepsPetStoreOrders.java
       └── resources
           ├── serenity.conf
           ├── dataJSON
           │   └── invalidOrder.json
           └── features
               └── petStoreOrders.feature
```
</details>

## Highlights
1. The use of `Tasks` with `Builders` for each endpoint enables easy reuse across different `Steps`, allowing them to be adapted to the needs of each test case while preventing duplication within the project. Additionally, this structure enhances code readability within the `Steps`, making maintenance and comprehension more simple.

2. In the project, random access to JSON objects was implemented to add dynamism to executions and reduce the reuse of the same test data. This was done recursively to prevent the same _payload_ from being repeated within a request. Additionally, this concept can be applied to other flows, such as login processes or element selection within a request. While it may not always be possible to choose completely random values due to business rules, a similar approach can help diversify test data and improve scenario coverage.

3. Using `@After`  tags in the test flow helps keep things clean by resetting test data after each run. This way, you can reuse test data without worrying about one test messing up the next. It makes test executions more reliable and prevents issues caused by leftover data or conflicting states.

## Findings

During the execution of automated tests for the `/store` service, the following observation and issues were identified:

| Priority | Issue | Expected Behavior | Actual Behavior |
|-----|------|------|------|
|${\textsf{\color{red}High}}$ | Order creation (POST) acting as updated (PUT) | Return `409`(Conflict) or `400` (Bad Request) | Updates existing order |
|${\textsf{\color{red}High}}$ | Duplication of order may result | Return `409`(Conflict) or `400` (Bad Request) | Creates order |
|${\textsf{\color{red}High}}$ | Empty payload | Return `400` (Bad Request) | Creates order with empty values | 
|${\textsf{\color{orange}Medium}}$ | Missing required fields | Return `400` (Bad Request) | Creates order with default `0` or omits fields |
|${\textsf{\color{orange}Medium}}$ | Deleting non-existent order | Return `404` (Not Found) | Returns `200` (OK) |

## Improvement

The following test cases and improvement related to `/store` service could be addressed in  future implementations. These will enhance test coverage and ensure the robustness of the API.

### Future automated test cases

1. Attempt to check a non-existent order
2. Attempt to create an order with a duplicate ID and identical values
3. Impact on inventory when creating an order with a duplicate ID and identical values
4. Attempt to create an order with a duplicate ID but different values
5. Impact on inventory when creating an order with a duplicate ID but different values

### Integration of E2E testing with CI/CD pipelines

Including E2E scenarios that cover the `/store`,`/pet` and `/user` services. These tests can be integrated into CI/CD pipelines to automatically validate each code change against the entire system, reducing the risk of errors.

### Setting of environment variables and test data variables
  - **Dynamic environment selection:** Modify the project to allow dynamic selection od the environment (_e.g.,_ QA, STG, DEV) through environment variables, which can be configured in the `serenity.conf` file or via command-line arguments.
  - **Secure handling of sensitive data:** For sensitive data (_e.g.,_ credentials, Authentication tokens, personal data, financial data), implement encrypted configuration files that can be decrypted at runtime. This ensures security while maintaining flexibility across environments.
  - **Dynamic test data handling:** Currently, some variables are hardcoded in the project. These can be externalized into files such as JSON, CSV or even a database.

## Runtime evidence
![image](https://github.com/user-attachments/assets/6667367b-2c40-4e42-8444-01f1a35e0d79)
![image](https://github.com/user-attachments/assets/9dedb08a-1740-4f02-8416-92ecc3f9306c)

# 2. API Perfomance Testing

### Built with
This project use the following technologies and tools:

- **IDE:** Visual Studio Code
- **Testing Platform:** k6
- **Programming Language:** Javascript
- **Environment:** Local

## Structure
<details open>
<summary><h5>The project is structured as follows (please click here to hide...)</h5></summary>

```
PetStorePerformance
└── Store (orders)
    ├── scripts
    │   ├── deleteOrder.js
    │   ├── getInventory.js
    │   ├── getOrder.js
    │   └── postOrder.js
    └── resources
        └── config.json
```
</details>

## Highlights

1. By using command-line variables can be dynamically configured the execution of each test script. This allows you to select the desired type of test to run (e.g., smoke, load, stress, peak).
2. It is important to note that in `postOrder`, each value of the fields in the payload is dynamically generated for each execution. This ensures that tests are realistic and avoid conflicts caused by duplicate or static data.
3. The project includes performance thresholds to ensure the system meets predefined criteria. In this case are used the following:
     - **Response Time:** If more than 5% of requests take longer than 500ms, the test will fail and if more than 10% of requests take longer than 800ms, the test will fail.
     - **Error Rate:** If there are more than 1% HTTP errors, the test will fail.

## Improvement
1. Additionally, a resources folder was created to store environment variables and URLs in the config.json file, allowing for future configuration management and easier script maintenance.
2. The current framework uses thresholds to validate some system behavior. A valuable improvement would be to abort the test if the system fails to recover within a specified time frame. This would save time and resources by stopping tests early when performance criteria are not met, avoiding prolonged execution of failing scenarios.
3. Include full E2E flow testing to evaluate the system's overall perfomance.

## Test Result
Performance testing was primarily focused on the delete service. This decision was made because this flow integrates two critical operations: **order creation** and **order deletion** (part of the CRUD operations). This is a key workflow in the `/Store` service, as it simulates a real-world scenario where users frequently create and cancel orders.

🔎 High transaction volume
🔎 Impact on inventory
🔎 Risk of bottlenecks 🔎

### Peak test evidence
![image](https://github.com/user-attachments/assets/a00ae116-9b8a-48ce-931f-d8059354a0fd)
| Metric | Value | Implication |
|-----|------|------|
| **Scenario** | 100 VUs (1 min) → 2000 VUs (8 min) → 0 VUs (3 min) | Simulation of a realistic traffic spike with rapid scaling |
| **Error rate** |96.8% (336275 failures out of 347387 requests)| The system could not handle the load, resulting in a high failure rate |
|**Error code** | `500` (Internal Server Error)| Server errors due to resource exhaustion or logic issues|
|**Response time**|Average: 77.35μs; Maximun: 87.51ms | Although the avarage time is low, the high failure rate indicates system overload |
|**Throughput**|521.3 requests/second|High request volume, but most of them failed due system limitations |
|**Thresholds crossed**|`http_reg_failed`(request failures)| The failure rate exceeded aceptable threshold, which indicates a severe issue under the load |

### Stress Test
![image](https://github.com/user-attachments/assets/7be0175a-46fc-42cf-b719-cfac4838f4e0)

## Conclusions
