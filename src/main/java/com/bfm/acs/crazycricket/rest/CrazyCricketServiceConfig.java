/**
 * 
 */
package com.bfm.acs.crazycricket.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Service configuration details
 * 
 * @author Kapil
 * Created: Jul 1, 2016
 *
 */
@Component
public class CrazyCricketServiceConfig {
	@Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	
	@Override
	public String toString(){
		return "http://" + serverAddress + ":" + serverPort;
	}
}
