package com.bfm.acs.crazycricket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.bfm.acs.crazycricket.data.DataStore;
import com.bfm.acs.crazycricket.kafka.KafkaProcessor;
import com.bfm.acs.crazycricket.rest.CrazyCricketServiceConfig;

/**
 * Main application class to publish REST API and trigger Kafka processor
 * 
 * @author Kapil
 * Created: Jun 30, 2016
 *
 */
@SpringBootApplication
public class Application{    
	public static void main(final String[] args) throws InterruptedException {
		//Trigger Spring application and REST service
    	ApplicationContext app = SpringApplication.run(Application.class, args);
    	CrazyCricketServiceConfig serviceConfig = app.getBean(CrazyCricketServiceConfig.class);
    	System.out.println("Crazy Cricket REST Service started at: " + serviceConfig.toString());
    	
    	//Trigger Kafka game event processor
    	DataStore dataStore = app.getBean(DataStore.class);
    	String[] topics = {"TEST", "TWENTY_TWENTY", "LIMITED_OVERS"};
    	KafkaProcessor kfp = new KafkaProcessor(dataStore, args[0], topics);
    	Thread processor = new Thread(kfp);
    	processor.start();
    }
}
