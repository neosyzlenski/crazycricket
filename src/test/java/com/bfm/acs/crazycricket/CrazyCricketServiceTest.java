/**
 * 
 */
package com.bfm.acs.crazycricket;

import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.bfm.acs.crazycricket.data.DataStore;
import com.bfm.acs.crazycricket.rest.CrazyCricketService;

/**
 * Test cases for our crazy service
 * 
 * @author Kapil
 * Created: Jul 1, 2016
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CrazyCricketServiceTest {
    @Mock
    private DataStore dataStore;

    @Mock
    private CrazyCricketService crazyCricketService;
    
    @Test
    public void testLeaderBoard(){
    	
    }
    
    @Test
    public void testLeaderBoardWithDateRange(){
    	
    }
    
    @Test
    public void testNationalLeaderBoard(){
    	
    }
    
    @Test
    public void testNationalLeaderBoardWithDateRange(){
    	
    }
}
