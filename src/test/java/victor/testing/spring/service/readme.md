# Migrate a Unit Test to an Integration Test

## 1. Change to @SpringBootTest
- replace the @ExtendWith with @SpringBootTest
- replace @Mock with @MockBean -> replaces the bean with a Mockito mock
- add @ActiveProfile("db-mem") -> reads application-db-mem.properties that changes the DB url to an in-mem H2 instance

## 2. Insert to DB instead of Mocking Repos
- instead of when(supplierRepo.findById), do a repo.save
- instead of verify(repo.save(captor)), do a repo.find
- replace @MockBean with @Autowired
- duplicate the createOk test. Running the whole test class still works?
- uncomment the assert on getCreateDate -> it should work now (not working when using Mockito)

## 3. Clean up the state from DB
- At the beginning of the test, there should be nothing in DB 
  - Write an assert checking that in a @BeforeEach
- Experiment with all these alternative solutions:
    a) in a @BeforeEach delete all suppliers and products via repo.deleteAll
    b) @Sql on the class level with phase=before each test, pointing to the script at classpath:/sql/cleanup.sql  
    c) @DirtiesContext -> blows up the entire Spring Context->has to restart = performance hit!
    d) @Trasactional on the test class [the best⭐️]

## 4. Testcontainers: Startup a real DB in a Docker for tests [optional]
- Extend BaseDatabaseTest
- Remove @ActiveProfile(db-mem)
- 

## 5. Emulate API responses with WireMock
- @AutoConfigureWireMock(port=0) to start a server replaying responses from src/test/resources/mappings/*
- Set the `safety.service.url.base=http://localhost:${wiremock.server.port}` via
- @TestPropertySource on class, or
- ActiveProfile("wiremock") reading from application-wiremock.properties
- Remove the @MockBean Client from test -> the real class now is used

## 6. API-level test - inspect
- Read into ProductApiTest