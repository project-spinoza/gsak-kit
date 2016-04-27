package org.projectspinoza.gephikit.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.projectspinoza.gephikit.Main;

public class ConfigurationManager {
	Configuration configuration;
	private static Logger log = Logger.getLogger(ConfigurationManager.class);
	
	public Configuration getConfiguration(){
		return configuration;
	}
	public void setInitialConfiguration(String filePath) {
		try{
		ObjectMapper mapper = new ObjectMapper();
		configuration = mapper.readValue(new File(filePath), Configuration.class);
		}catch(Exception ex){
			log.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
}
