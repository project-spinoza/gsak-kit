package org.projectspinoza.gephikit.configuration;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ConfigurationManager {
	Configuration configuration;
	
	public Configuration getConfiguration(){
		return configuration;
	}
	public void setInitialConfiguration(String filePath) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		configuration = mapper.readValue(new File(
				filePath), Configuration.class);
		
	}
	
}
