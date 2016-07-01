/**
 * 
 */
package com.bfm.acs.crazycricket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bfm.acs.crazycricket.data.DataStore;
import com.bfm.acs.crazycricket.kafka.KafkaProcessor;
import com.bfm.acs.crazycricket.rest.CrazyCricketService;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test cases for our crazy service
 * 
 * @author Kapil
 * Created: Jul 1, 2016
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, TestConfig.class })
@WebAppConfiguration
public class CrazyCricketServiceTest {
	@InjectMocks
    private CrazyCricketService service;
    
	@Autowired
    private WebApplicationContext context;
	
	private MockMvc mvc;
	
	private KafkaProcessor kfp;
	
	@Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        String[] topics = {"TEST", "TWENTY_TWENTY", "LIMITED_OVERS"};
    	DataStore dataStore = context.getBean(DataStore.class);
    	kfp = new KafkaProcessor(dataStore, "localhost:9092", topics);
    	Thread processor = new Thread(kfp);
    	processor.start();
    }
    
    @After
    public void tearDown(){
    	kfp.stop();
    }
    
    @Test
    public void testNationalLeaderBoard() throws Exception{    	
    	mvc.perform(get("/api/national_leaderboard")
                	.accept(MediaType.APPLICATION_JSON))
                	.andExpect(status().isOk())
                	.andExpect(jsonPath("$", hasSize(4)));
    }
}
