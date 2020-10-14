package victor.testing.kafka;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class EmbeddedKafkaTest {

    private static final String LISTEN_TO_TOPIC = "dummylistenertopic";
    private static final String SEND_TO_TOPIC = "dummyproducertopic";

    @ClassRule
    public static final EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, false, 5, LISTEN_TO_TOPIC, SEND_TO_TOPIC);

    @Autowired
    private KafkaProperties properties;

    private static EmbeddedKafkaBroker embeddedKafka;
    KafkaTemplate<String, String> kafkaTemplate;
    Map<String, Object> producerProps;
    DefaultKafkaProducerFactory<String, String> producerFactory;

    @BeforeClass
    public static void setup() {
        embeddedKafka = embeddedKafkaRule.getEmbeddedKafka();
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
    }

    @Before
    public void kafkaSetup() {
        producerProps = properties.buildProducerProperties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId-" + UUID.randomUUID());
        producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);

    }

    @Test
    public void totpMeansEnrollmentCompletedTest() {
        // Arrange

        String event = new String("test");

        // Act
        kafkaTemplate.send(new ProducerRecord<>(LISTEN_TO_TOPIC, event));

        // Assert
        Consumer<String, NotificationEvent> consumer = getNotificationConsumer("consumer-" + UUID.randomUUID());
        ConsumerRecords<String, NotificationEvent> records = KafkaTestUtils.getRecords(consumer);
        consumer.commitSync();

        NotificationEvent notificationEvent = records.iterator().next().value();

        Assert.assertEquals("email@example.com", notificationEvent.getNotification());
    }


    private Consumer<String, NotificationEvent> getNotificationConsumer(String consumerGroup) {
        Map<String, Object> consumerProps = properties.buildConsumerProperties();
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 5000);
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
        ConsumerFactory<String, NotificationEvent> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, NotificationEvent> consumer = cf.createConsumer();
        consumer.subscribe(Collections.singleton(SEND_TO_TOPIC));
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, SEND_TO_TOPIC);

        return consumer;
    }
}
