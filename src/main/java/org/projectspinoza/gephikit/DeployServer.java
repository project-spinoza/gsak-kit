package org.projectspinoza.gephikit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.projectspinoza.gephikit.configuration.ConfigurationManager;
import org.projectspinoza.gephikit.datasource.SigmaGraph;
import org.projectspinoza.gephikit.layouts.LayoutManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeployServer extends AbstractVerticle {
	String response;
	ConfigurationManager configurationManager;
	ObjectMapper mapper;
	BasicGraph basicGraph;
	SigmaGraph sigmaGraph;
	LayoutManager layoutManager;

	HttpServer server;
	Router router;
	private static org.apache.log4j.Logger log = Logger.getLogger(DeployServer.class);

	public DeployServer(String configurationFilePath)
			throws JsonParseException, JsonMappingException, IOException {
		initialize(configurationFilePath);
		

	}

	/**
	 * Initializing objects
	 * @throws IOException 
	 * @throws org.codehaus.jackson.map.JsonMappingException 
	 * @throws org.codehaus.jackson.JsonParseException 
	 */
	public void initialize(String configurationFilePath) throws org.codehaus.jackson.JsonParseException, org.codehaus.jackson.map.JsonMappingException, IOException {
		mapper = new ObjectMapper();
		configurationManager = new ConfigurationManager();
		configurationManager.setInitialConfiguration(configurationFilePath);
	}
	
    /**
     * Deploying the verical
     */
	@Override
	public void start() {

		server = vertx.createHttpServer();
		router = Router.router(vertx);
		router.route().handler(CorsHandler.create("*").allowedMethod(HttpMethod.GET)
									.allowedMethod(HttpMethod.POST)
									.allowedMethod(HttpMethod.OPTIONS)
									.allowedHeader("Content-Type, Authorization"));
		// registering different route handlers	
		registerHandlers();

		server.requestHandler(router::accept).listen(
				configurationManager.getConfiguration().getPort(),
				configurationManager.getConfiguration().getHost());
	}
	
    /**
     * routes for different requests 
     */
	
	private void registerHandlers() {
        //gephi route  to generate simple gephi graph
		router.route("/gephi").blockingHandler(routingContext -> {
			
					String graphJson = "";
					HttpServerResponse response = routingContext.response();
					
					try {
						    basicGraph = getBasicgraph(); 
							graphJson = getSigmaGraph(basicGraph.getGraphModel().getDirectedGraph());
						}catch (NoNodeAvailableException e) {
							log.error("ElasticSearch Connectivity Error ");
                         graphJson = "{error : ElasticSearch Connectivity Error }";
						}catch (IOException ioException) {
							log.error("Exception Reading Text File");
							 graphJson = "{error : Exception Reading Text File}";
							 
						}catch(Exception ex){
							 ex.printStackTrace();
							 graphJson = "{error:"+ex.getMessage()+"}";
						}
					
					response.end(graphJson);
				});
		//layout route to generate graph with the required layout
		router.route("/layout").blockingHandler(
				routingContext -> {
					HttpServerResponse response = routingContext.response();
					String graphJson = "";
					try {
						HashMap<String, Object> settings = mapper.readValue(
								routingContext.request().getParam("settings"),
								new TypeReference<HashMap<String, Object>>() {
								});
						
						basicGraph = getBasicgraph();
						applyLayout(settings, basicGraph.graphModel);
						graphJson = getSigmaGraph(basicGraph.graphModel.getDirectedGraph());

					}catch (NoNodeAvailableException e) {
						log.error("ElasticSearch Connectivity Error ");
                     graphJson = "{error : ElasticSearch Connectivity Error }";
					}catch (IOException ioException) {
						log.error("Exception Reading Text File");
						 graphJson = "{error : Exception Reading Text File}";
						 
					}catch(Exception ex){
						 ex.printStackTrace();
						 graphJson = "{error:"+ex.getMessage()+"}";
					}
				
				response.end(graphJson);
					
					
					
					

				});
	}
   /**
    * use to apply layouts on the given graph model
    * @param settings
    * @param graphModel
    * @throws Exception
    */
	public void applyLayout(Map<String, Object> settings, GraphModel graphModel) throws Exception {
		
		configurationManager.getConfiguration().setSelectedLayout(settings.get("name").toString().trim());
		
		if (settings.get("name").toString().trim().equals("YifanHuLayout")) {
			configurationManager.getConfiguration().getLayout().getYiFanHuLayout().setDistance((int) settings.get("distance"));
			configurationManager.getConfiguration().getLayout().getYiFanHuLayout().setIteration((int) settings.get("iteration"));
		}
		
		 new LayoutManager(configurationManager.getConfiguration().getSelectedLayout(),graphModel,configurationManager.getConfiguration());
		
	
	}

	
    /**
     * use to get basic gephi graph
     * @return
     * @throws Exception
     */
	public BasicGraph getBasicgraph() throws Exception {
		return new BasicGraph(configurationManager.getConfiguration());
	}
   
	/**
	 * use to generate sigmagraph from given graph
	 * @param graph
	 * @return
	 * @throws JsonProcessingException
	 */
	public String getSigmaGraph(Graph graph) throws JsonProcessingException {
		sigmaGraph = new SigmaGraph();
		sigmaGraph.build(graph, configurationManager.getConfiguration());
		return mapper.writeValueAsString(sigmaGraph);
	}
	/******************* getter and setter funtions **********************/
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
