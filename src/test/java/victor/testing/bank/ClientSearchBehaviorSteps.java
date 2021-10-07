package victor.testing.bank;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import victor.testing.bank.entity.Client;
import victor.testing.bank.repo.ClientJpaRepository;
import victor.testing.bank.vo.ClientSearchCriteria;
import victor.testing.bank.vo.ClientSearchResult;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = BankApplication.class)
public class ClientSearchBehaviorSteps {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ClientJpaRepository repository;

    private Client client;
    private ClientSearchCriteria criteria = new ClientSearchCriteria();

    @Given("^A Client exists in DB$")
    public void a_Client_exists_in_DB() throws Throwable {
        client = new Client();
        entityManager.persist(client);
    }

    @Given("^The Client has name \"([^\"]*)\"$")
    public void theClientHasName(String clientName) throws Throwable {
        client.setName(clientName);
    }

    @When("^Search criteria name=\"([^\"]*)\"$")
    public void searchCriteriaName(String clientNameCriteria) throws Throwable {
        criteria.setName(clientNameCriteria);
    }

    @Then("^The Client is returned$")
    public void theClientIsReturned() {
        List<ClientSearchResult> list = repository.search(criteria);
        assertEquals(1, list.size());
        assertEquals((long)client.getId(), list.get(0).getId());
        assertEquals(client.getName(), list.get(0).getName());
    }

    @Then("^No results are returned$")
    public void noResultsAreReturned() {
        List<ClientSearchResult> list = repository.search(criteria);
        assertEquals(0, list.size());
    }

    @Given("^The Client has nationality iso \"([^\"]*)\"$")
    public void theClientHasNationalityIso(String nationalityIso) throws Throwable {
        client.setNationalityIso(nationalityIso);
    }

    @When("^Search criteria nationality iso = \"([^\"]*)\"$")
    public void searchCriteriaNationalityIso(String isoCsv) throws Throwable {
        criteria.setNationalityIsoList(Arrays.asList(isoCsv.split(",")));
    }
}
