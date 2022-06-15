package victor.testing.tools;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

public class CucumberSpringTransactionHooks implements BeanFactoryAware {

    private BeanFactory beanFactory;
    private TransactionStatus transactionStatus;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Before(value = "@txn", order = 100)
    public void startTransaction() {
        Assert.notNull(beanFactory, "Spring Context is not started. Have you uncommented the @CucumberContextConfiguration ?");
        transactionStatus = beanFactory.getBean(PlatformTransactionManager.class)
                .getTransaction(new DefaultTransactionDefinition());
    }

    @After(value = "@txn", order = 100)
    public void rollBackTransaction() {
        beanFactory.getBean(PlatformTransactionManager.class)
                .rollback(transactionStatus);
    }

}