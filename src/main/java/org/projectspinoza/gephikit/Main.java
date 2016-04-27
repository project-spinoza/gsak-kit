package org.projectspinoza.gephikit;

import io.vertx.core.Vertx;



import org.apache.log4j.Logger;


public class Main { 
	private static Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args){
    	if(args.length == 0){
    		System.out.println("The Confiurarion file path is not pass in the arguments");
    		log.error("Configuration file is not given in commandline arguments");
    		System.exit(0);
    	}
    	 Vertx vertx = Vertx.vertx();
    	 vertx.deployVerticle(new Server(args[0]));
    }
}
