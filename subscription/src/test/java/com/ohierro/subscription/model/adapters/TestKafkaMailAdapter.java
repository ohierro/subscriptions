package com.ohierro.subscription.model.adapters;

import com.ohierro.subscription.model.ports.Subscription;
//import kafka.api.FetchRequest;
//import kafka.api.FetchRequestBuilder;
//import kafka.javaapi.consumer.SimpleConsumer;
//import kafka.message.MessageAndOffset;
//import kafka.server.KafkaConfig;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.test.TestingServer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumerTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class TestKafkaMailAdapter {

    @Autowired
    private KafkaMailAdapter mailAdapter;

//    private KafkaLocalServer kafkaLocalServer;
//
//    private static KafkaLocalServer kafkaLocalServer;
//    private static final String DEFAULT_KAFKA_LOG_DIR = "/tmp/test/kafka_embedded";
//    private static final String TEST_TOPIC = "test_topic";
//    private static final int BROKER_ID = 0;
//    private static final int BROKER_PORT = 5000;
//    private static final String LOCALHOST_BROKER = String.format("localhost:%d", BROKER_PORT);
//
//    private static final String DEFAULT_ZOOKEEPER_LOG_DIR = "/tmp/test/zookeeper";
//    private static final int ZOOKEEPER_PORT = 2000;
//    private static final String ZOOKEEPER_HOST = String.format("localhost:%d", ZOOKEEPER_PORT);
//
//
//    @BeforeClass
//    public static void startKafka(){
//        Properties kafkaProperties;
//        Properties zkProperties;
//
//        try {
//            //load properties
//            kafkaProperties = getKafkaProperties(DEFAULT_KAFKA_LOG_DIR, BROKER_PORT, BROKER_ID);
//            zkProperties = getZookeeperProperties(ZOOKEEPER_PORT,DEFAULT_ZOOKEEPER_LOG_DIR);
//
//            //start kafkaLocalServer
//            kafkaLocalServer = new KafkaLocalServer(kafkaProperties, zkProperties);
//            Thread.sleep(5000);
//        } catch (Exception e){
//            e.printStackTrace(System.out);
//            fail("Error running local Kafka broker");
//            e.printStackTrace(System.out);
//        }
//
//        //do other things
//    }

    private static final String BROKER_HOST = "localhost";
    private static final int BROKER_PORT = 2181;
    private static final String TEST_TOPIC = "mail";

    private Charset charset = Charset.forName("UTF-8");
    private CharsetDecoder decoder = charset.newDecoder();

    private static KafkaServerStartable kafka;
    private static TestingServer zookeeper;

    private KafkaMessageListenerContainer<String, Subscription> container;

    private BlockingQueue<ConsumerRecord<String, Subscription>> records;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, TEST_TOPIC);

    @BeforeClass
    public static void setUp() throws Exception {
//        zookeeper = new TestingServer(BROKER_PORT);
//
//        Properties properties = getKafkaProperties("", 9092, 0);
//        KafkaConfig kafkaConfig = new KafkaConfig(properties);
//        kafka = new KafkaServerStartable(kafkaConfig);
//        kafka.startup();
    }

    @Before
    public void setUp2() {
        setUpConsumer();
    }


    @Test
    public void testSendEmail() throws InterruptedException {
        mailAdapter.sendEmail(Subscription.builder()
        .email("fake1@email.com")
        .newsletterId("TEST_NEWSLETTER")
        .consent(true)
        .dateOfBirth(LocalDate.of(1981, 10, 9))
        .gender("male")
        .firstName("John")
        .build());


        log.debug("test");

        // check that the message was received
        ConsumerRecord<String, Subscription> received =
                records.poll(10, TimeUnit.SECONDS);

        received.offset();

        // Hamcrest Matchers to check the value
//        assertThat(received., hasValue(greeting));
        // AssertJ Condition to check the key
//        assertThat(received).has(key(null));
//        SimpleConsumer kafkaconsumer = getNewConsumer(BROKER_PORT, "test_client");
//
//        List<String> actualMessageList = new ArrayList<>();
//        long offset = 0l;
//
//        while (offset < 1) { // to avoid endless loop
//            FetchRequest request = new FetchRequestBuilder().clientId("clientName").addFetch(TEST_TOPIC, 0, offset, 1000000).build();
//            kafka.javaapi.FetchResponse fetchResponse = kafkaconsumer.fetch(request);
//
//            for(MessageAndOffset messageAndOffset : fetchResponse.messageSet(TEST_TOPIC, 0)) {
//                offset = messageAndOffset.offset();
//                String message = byteBufferToString(messageAndOffset.message().payload()).toString();
//                System.out.println("Consuming message - " + message);
//                actualMessageList.add(message);
//            }
//        }
//        assertEquals(1, actualMessageList.size());
////        assertEquals(actualMessageList, expectedMessageList);
    }

    private static Properties getKafkaProperties(String logDir, int port, int brokerId) {
        Properties properties = new Properties();
        properties.put("port", port + "");
        properties.put("broker.id", brokerId + "");
//        properties.put("log.dir", logDir);
        properties.put("zookeeper.connect", BROKER_HOST);
        properties.put("default.replication.factor", "1");
        properties.put("delete.topic.enable", "true");
        return properties;
    }

//    public static SimpleConsumer getNewConsumer(int port, String clientId) {
//        return new SimpleConsumer("localhost", port, 10000, 1024000, clientId);
//    }

    private static Properties getZookeeperProperties(int port, String zookeeperDir) {
        Properties properties = new Properties();
        properties.put("clientPort", port + "");
        properties.put("dataDir", zookeeperDir);
        return properties;
    }

    public void setUpConsumer() {

//        embeddedKafka.kafkaPorts(9092);

        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false",
                        embeddedKafka.getEmbeddedKafka());

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, Subscription> consumerFactory =
                new DefaultKafkaConsumerFactory<String, Subscription>(
                        consumerProperties);

        // set the topic that needs to be consumed
        ContainerProperties containerProperties =
                new ContainerProperties(TEST_TOPIC);

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory,
                containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container
                .setupMessageListener(new MessageListener<String, Subscription>() {
                    @Override
                    public void onMessage(
                            ConsumerRecord<String, Subscription> record) {
                        log.debug("test-listener received message='{}'",
                                record.toString());
                        records.add(record);
                    }
                });

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container,
                embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
    }

    public String byteBufferToString(ByteBuffer buffer)
    {
        String data = "";
        try {
            int old_position = buffer.position();
            data = decoder.decode(buffer).toString();
            // reset buffer's position to its original so it is not altered:
            buffer.position(old_position);
        }
        catch (Exception e) {
            return data;
        }
        return data;
    }
}
