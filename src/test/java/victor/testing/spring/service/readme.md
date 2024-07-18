# Migrate a Unit Test to an Integration Test

## 1. Change to @SpringBootTest
- replace the @ExtendWith with @SpringBootTest
- replace @Mock with @MockBean -> replaces the bean with a Mockito mock
- replace @InjectMocks with @Autowired -> a @SpringBootTest class feels like a Spring bean
- add @ActiveProfiles("test") -> reads application-test.properties that changes the DB url to an in-mem H2 instance

## 2. Insert to DB instead of Mocking Repos
- instead of when(supplierRepo.findById), do a repo.save
- instead of verify(repo.save(captor)), do:
  - a. repo.findByName, or
  - b. repo.findById(productId) and return the id from tested code 
- replace @MockBean with @Autowired; only SafetyClient remains a @MockBean
- duplicate the createOk test, and run the whole test class. Does it still works?
- uncomment the assert on getCreateDate -> it should work now (not working when using Mockito)

## 3. Clean up the state from DB
- At the beginning of the test, there should be nothing in DB 
  - Write an assert checking that in a @BeforeEach
- Experiment with all these alternative solutions:
    - a) in a @BeforeEach delete all products and suppliers via repo.deleteAll
    - b) @Sql on the class level with phase=before each test, pointing to the script at classpath:/sql/cleanup.sql  
    - c) @DirtiesContext -> blows up the entire Spring Context -> has to restart -> performance hit on CI build time
    - d) @Transactional on the test class [the best⭐️]: runs each @Test in its own transcation,
      automatically rolled back after each test (not commited as @Transactional in PROD)

## 4. Emulate API responses with WireMock
- @AutoConfigureWireMock(port=0) on class to start a WireMock HTTP server
  (replaying responses from src/test/resources/mappings/*)
- Set the property `safety.service.url.base=http://localhost:${wiremock.server.port}` via
  - @TestPropertySource(properties=) on class, or
  - add to @ActiveProfiles the "wiremock" profile to read more props from application-wiremock.properties
- Stop mocking the SafetyClient:
  - Remove the SafetyClient field from test -> the real class now is used
  - remove the when..then (not needed) 

## 5. Startup a real DB in a Docker for tests using Testcontainers [optional]
- extend BaseDatabaseTest and explore that class
- remove from your class @ActiveProfile, @SpringBootTest and @Transactional (they are inherited from base test class)
- change the name of the PRODUCT table in ONE incremental script from src/main/resources/db/migration => Test fails?

## 6. API-level test - inspect
- Read into ProductApiTest