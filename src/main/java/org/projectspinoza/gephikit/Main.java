package org.projectspinoza.gephikit;

import io.vertx.core.Vertx;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

public class Main {
	private static org.apache.log4j.Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException{
    	if(args.length == 0){
    		System.out.println("The Confiurarion file path is not pass in the arguments");
    		log.error("Configuration file is not given in commandline arguments");
    		System.exit(0);
    	}
    	 Vertx vertx = Vertx.vertx();
    	 vertx.deployVerticle(new DeployServer(args[0]));
  
    }
}
