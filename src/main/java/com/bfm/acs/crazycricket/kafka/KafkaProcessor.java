/**
 * 
 */
package com.bfm.acs.crazycricket.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfm.acs.crazycricket.CrazyCricketProtos.Game;
import com.bfm.acs.crazycricket.data.DataStore;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Processor thread for consuming game events and persisting them to data store
 * 
 * @author Kapil
 * Created: Jun 30, 2016
 *
 */
public class KafkaProcessor implements Runnable{
	private static final Logger LOG = LoggerFactory.getLogger(KafkaProcessor.class);
	
	private final KafkaConsumer<String, byte[]> consumer;
	private final DataStore 					dataStore;
	private final String						kafkaBroker;
	private final String[]						topics;

	//Flag to kill the processor
	private AtomicBoolean stopped = new AtomicBoolean(false);
	
	public KafkaProcessor(DataStore dataStore, String kafkaBroker, String[] topics){
		this.dataStore = dataStore;
		this.kafkaBroker = kafkaBroker;
		this.topics = topics;
		this.consumer = new KafkaConsumer<String, byte[]>(getKafkaConfig());
		
		LOG.info(String.format("Intialized Kafka processor for topic(s): %s", Arrays.toString(topics)));
	}
	
	@Override 
	public void run(){
		try{
			consumer.subscribe(Arrays.asList(this.topics));
			
			while(!stopped.get()){
				ConsumerRecords<String, byte[]> records = consumer.poll(1000);
				for (ConsumerRecord<String, byte[]> record : records) {
					Game game = Game.parseFrom(record.value());
					dataStore.save(game);
				}
			}
		}catch(WakeupException ex){
			LOG.error("Error while consuming game events", ex);
			// Ignore exception if closing
            if (!stopped.get()) throw ex;
		} catch (InvalidProtocolBufferException ex) {
			LOG.error("Unable to parse game event data", ex);
		}finally{
			consumer.close();
		}
	}
	
	/**
	 * To stop the train pull the chain 
	 */
	public void stop(){
		LOG.info("Stopping kafka processor...");
		this.stopped.set(true);
		consumer.wakeup();
	}
	
	/**
	 * Helper to setup and return Kafka consumer configuration
	 * 
	 * @return config
	 */
	private Properties getKafkaConfig(){
		Properties props = new Properties();
		
		props.put("bootstrap.servers", this.kafkaBroker);
	    props.put("group.id", "test");
	    props.put("enable.auto.commit", "true");
	    props.put("auto.commit.interval.ms", "1000");
	    props.put("session.timeout.ms", "30000");
	    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		
	    return props;
	}
}
