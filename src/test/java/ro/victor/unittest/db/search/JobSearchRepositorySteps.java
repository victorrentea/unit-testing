//package ro.victor.unittest.db.search;
//
//import cucumber.api.PendingException;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class JobSearchRepositorySteps {
//    @Autowired
//    private JobRepository repo;
//
//    @Given("^A job exists with name \"([^\"]*)\"$")
//    public void aJobExistsWithName(String jobName) throws Throwable {
//        Job job = new Job();
//        job.setName(jobName);
//        repo.save(job);
//    }
//
//
//    @Then("^I find the job$")
//    public void i_find_the_job() throws Throwable {
//        // Write code here that turns the phrase above into concrete actions
//        throw new PendingException();
//    }
//
//
//    @When("^I search by name \"([^\"]*)\"$")
//    public void iSearchByName(String searchName) throws Throwable {
//        // Write code here that turns the phrase above into concrete actions
//        throw new PendingException();
//    }
//}
