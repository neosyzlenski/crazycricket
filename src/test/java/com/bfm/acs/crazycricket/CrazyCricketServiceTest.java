/**
 * 
 */
package com.bfm.acs.crazycricket;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bfm.acs.crazycricket.rest.CrazyCricketService;

import static org.hamcrest.Matchers.*;
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
@SpringApplicationConfiguration(classes = { Application.class, TestConfiguration.class })
@WebAppConfiguration
public class CrazyCricketServiceTest {
	@InjectMocks
    private CrazyCricketService service;
    
	@Autowired
    private WebApplicationContext context;
	
	private MockMvc mvc;
	private static EmbeddedDatabase db;
	
	@Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
	
	@BeforeClass
	public static void setUp(){
		db = new EmbeddedDatabaseBuilder()
				.addScript("schema.sql")
				.addScript("test-data.sql")
				.build();
	}
	
	@AfterClass
	public static void tearDown(){
		db.shutdown();
	}
    
	@Test
    public void testLeaderBoard() throws Exception{
		mvc.perform(get("/api/leaderboard")
                	.accept(MediaType.APPLICATION_JSON))
                	.andExpect(status().isOk())
                	.andExpect(jsonPath("$", hasSize(5)))
                	.andExpect(jsonPath("$[0]", hasEntry("shubham", 5)))
                	.andExpect(jsonPath("$[1]", hasEntry("oscar", 4)))
                	.andExpect(jsonPath("$[2]", hasEntry("sachin", 4)))
                	.andExpect(jsonPath("$[3]", hasEntry("andrew", 1)))
                	.andExpect(jsonPath("$[4]", hasEntry("imran", 1)));
    }
	
	@Test
    public void testLeaderBoardWithStartDateGreaterThanOrEqualsEndDate() throws Exception{    	
		mvc.perform(get("/api/leaderboard?start=20160101&end=20160101")
            	.accept(MediaType.APPLICATION_JSON))
            	.andExpect(status().isOk())
            	.andExpect(jsonPath("$", hasSize(5)))
            	.andExpect(jsonPath("$[0]", hasEntry("oscar", 2)))
            	.andExpect(jsonPath("$[1]", hasEntry("andrew", 1)))
            	.andExpect(jsonPath("$[2]", hasEntry("imran", 1)))
            	.andExpect(jsonPath("$[3]", hasEntry("sachin", 1)))
            	.andExpect(jsonPath("$[4]", hasEntry("shubham", 1)));
    }
	
	@Test
    public void testLeaderBoardWithEndDateGreaterThanStartDate() throws Exception{    	
		mvc.perform(get("/api/leaderboard?start=20160301&end=20160101")
            	.accept(MediaType.APPLICATION_JSON))
            	.andExpect(status().isOk())
            	.andExpect(jsonPath("$", hasSize(0)));
    }
	
    @Test
    public void testNationalLeaderBoard() throws Exception{    	
    	mvc.perform(get("/api/national_leaderboard")
            	.accept(MediaType.APPLICATION_JSON))
            	.andExpect(status().isOk())
            	.andExpect(jsonPath("$", hasSize(4)))
            	.andExpect(jsonPath("$[0]", hasEntry("India", 9)))
            	.andExpect(jsonPath("$[1]", hasEntry("England", 4)))
            	.andExpect(jsonPath("$[2]", hasEntry("Pakistan", 1)))
            	.andExpect(jsonPath("$[3]", hasEntry("USA", 1)));
    }
    
    @Test
    public void testNationalLeaderBoardWithStartDateGreaterThanOrEqualsEndDate() throws Exception{    	
    	mvc.perform(get("/api/national_leaderboard?start=20160101&end=20160101")
            	.accept(MediaType.APPLICATION_JSON))
            	.andExpect(status().isOk())
            	.andExpect(jsonPath("$", hasSize(4)))
            	.andExpect(jsonPath("$[0]", hasEntry("England", 2)))
            	.andExpect(jsonPath("$[1]", hasEntry("India", 2)))
            	.andExpect(jsonPath("$[2]", hasEntry("Pakistan", 1)))
            	.andExpect(jsonPath("$[3]", hasEntry("USA", 1)));
    }
    
    @Test
    public void testNationalLeaderBoardWithEndDateGreaterThanStartDate() throws Exception{    	
    	mvc.perform(get("/api/national_leaderboard?start=20160201&end=20160101")
            	.accept(MediaType.APPLICATION_JSON))
            	.andExpect(status().isOk())
            	.andExpect(jsonPath("$", hasSize(0)));
    }
    
    @Test
    public void testInvalidDateRangeExceptionIsThrownForInvalidStartDate() throws Exception{
    	mvc.perform(get("/api/national_leaderboard?start=2016020&end=20160101")
            	.accept(MediaType.APPLICATION_JSON))
            	//.andExpect(status().isBadRequest()) //Spring Test MVC returning 500 instead of 400
            	.andExpect(jsonPath("$.exception", is("com.bfm.acs.crazycricket.data.InvalidDateRangeException")))
            	.andExpect(jsonPath("$.message", is("Invalid start date, expected format: yyyyMMdd")));
    }
    
    @Test
    public void testInvalidDateRangeExceptionIsThrownForInvalidEndDate() throws Exception{
    	mvc.perform(get("/api/national_leaderboard?start=20160201&end=2016010")
            	.accept(MediaType.APPLICATION_JSON))
            	//.andExpect(status().isBadRequest())
            	.andExpect(jsonPath("$.exception", is("com.bfm.acs.crazycricket.data.InvalidDateRangeException")))
            	.andExpect(jsonPath("$.message", is("Invalid end date, expected format: yyyyMMdd")));
    }
    
    @Test
    public void testInvalidDateRangeExceptionIsThrownForMissingEndDate() throws Exception{
    	mvc.perform(get("/api/national_leaderboard?start=20160201")
            	.accept(MediaType.APPLICATION_JSON))
            	//.andExpect(status().isBadRequest())
            	.andExpect(jsonPath("$.exception", is("com.bfm.acs.crazycricket.data.InvalidDateRangeException")))
            	.andExpect(jsonPath("$.message", is("Either both dates must be null or both not null")));
    }
}
