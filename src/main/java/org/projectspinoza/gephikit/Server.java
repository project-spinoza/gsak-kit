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
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.ranking.api.RankingController;
import org.openide.util.Lookup;
import org.projectspinoza.gephikit.configuration.ConfigurationManager;
import org.projectspinoza.gephikit.datasource.SigmaGraph;
import org.projectspinoza.gephikit.filters.FilterImplemintation;
import org.projectspinoza.gephikit.filters.GraphFilter;
import org.projectspinoza.gephikit.filters.GraphPreview;


import org.projectspinoza.gephikit.layouts.LayoutManager;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Server extends AbstractVerticle {
	String response;
	ConfigurationManager configurationManager;
	ObjectMapper mapper;
	BasicGraph basicGraph;
	SigmaGraph sigmaGraph;
	LayoutManager layoutManager;
	AttributeModel attributeModel;
	GraphFilter graphFilter;
	RankingController rankingController;
	FilterImplemintation filterImplementation ;
	HttpServer server;
	Router router;
	GraphPreview graphPreview;
	private static Logger log = Logger.getLogger(Server.class);

	public Server(String configurationFilePath) {
		initialize(configurationFilePath);
		

	}

	/**
	 * Initializing objects
	 * @throws org.codehaus.jackson.map.JsonMappingException 
	 * @throws org.codehaus.jackson.JsonParseException 
	 * 
	 * @throws IOException
	 * @throws org.codehaus.jackson.map.JsonMappingException
	 * @throws org.codehaus.jackson.JsonParseException
	 */
	public void initialize(String configurationFilePath){
		mapper = new ObjectMapper();
		configurationManager = new ConfigurationManager();
		configurationManager.setInitialConfiguration(configurationFilePath);
				graphFilter = new GraphFilter();
		rankingController = Lookup.getDefault().lookup(RankingController.class);
		filterImplementation = new FilterImplemintation();
		graphPreview = new GraphPreview(); 
		
	}

	/**
	 * Deploying the verical
	 */
	@Override
	public void start() {
		server = vertx.createHttpServer();
		router = Router.router(vertx);
		router.route().handler(CorsHandler.create("*").allowedMethod(HttpMethod.GET)
						.allowedHeader("Content-Type, Authorization")
						);
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
		// welcome route with welcome message
		router.route("/").blockingHandler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.end("<h1>Welcome to gsak kit</h1>");
	 });
		
		// gephi route to generate simple gephi graph
		router.route("/gephi").blockingHandler(routingContext -> {
			String graphJson = "";
			HttpServerResponse response = routingContext.response();
			try {
				if (routingContext.request().getParam("basicSettings") != null) {
					HashMap<String, Object> basicCofiguration = mapper.readValue(routingContext	.request().getParam("basicSettings"),new TypeReference<HashMap<String, Object>>() {});
					applyBasicConfiguration(basicCofiguration,configurationManager);
				}
				basicGraph = getBasicgraph();
				graphJson = getSigmaGraph(basicGraph.getGraphModel().getDirectedGraph());
			}catch (Exception ex) {
				ex.printStackTrace();
				graphJson = "{error:" + ex.getMessage() + "}";
			}

			response.end(graphJson);
	 });
		
	// layout route to generate graph with the required layout
		router.route("/layout").blockingHandler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			String graphJson = "";
			try {
				HashMap<String, Object> settings = mapper.readValue(routingContext.request().getParam("layoutSettings"),new TypeReference<HashMap<String, Object>>() {});
				if (routingContext.request().getParam("basicSettings") != null) {
					HashMap<String, Object> basicCofiguration = mapper.readValue(routingContext.request().getParam("basicSettings"),new TypeReference<HashMap<String, Object>>() {});
					applyBasicConfiguration(basicCofiguration,configurationManager);
				}
				basicGraph = getBasicgraph();
				applyLayout(settings, basicGraph.graphModel);
				graphJson = getSigmaGraph(basicGraph.graphModel.getDirectedGraph());
			}catch (Exception ex) {
				ex.printStackTrace();
				graphJson = "{error:" + ex.getMessage() + "}";
			}
			response.end(graphJson);
		});
		
		// filter route to generate graph with the required filters
		router.route("/filter").blockingHandler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			String graphJson = "";
			try {
				HashMap<String, Object> settings = mapper.readValue(routingContext.request().getParam("filterSettings"),new TypeReference<HashMap<String, Object>>() {});
				if (routingContext.request().getParam("basicSettings") != null) {
					HashMap<String, Object> basicCofiguration = mapper.readValue(routingContext.request().getParam("basicSettings"),new TypeReference<HashMap<String, Object>>() {});
					applyBasicConfiguration(basicCofiguration,configurationManager);
				}
				basicGraph = getBasicgraph();
				applyFilters(settings, basicGraph.graphModel.getGraph());
				graphJson = getSigmaGraph(basicGraph.graphModel.getDirectedGraph());
			}catch (Exception ex) {
				ex.printStackTrace();
				graphJson = "{error:" + ex.getMessage() + "}";
			}
			response.end(graphJson);

		});
		
	// gsakkit route to generate graph with the required filters and layouts
		router.route("/gsakkit").blockingHandler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			String graphJson = "";
			try {
				if (routingContext.request().getParam("basicSettings") != null) {
					HashMap<String, Object> basicCofiguration = mapper.readValue(routingContext.request().getParam("basicSettings"),new TypeReference<HashMap<String, Object>>() {});
					applyBasicConfiguration(basicCofiguration,configurationManager);
				}
				basicGraph = getBasicgraph();
				if (routingContext.request().getParam("filterSettings") != null) {
					HashMap<String, Object> filters = mapper.readValue(routingContext.request().getParam("filterSettings"),new TypeReference<HashMap<String, Object>>() {});
					applyFilters(filters, basicGraph.graphModel.getGraph());
				}
				if (routingContext.request().getParam("layoutSettings") != null) {
					HashMap<String, Object> layouts = mapper.readValue(routingContext.request().getParam("layoutSettings"),new TypeReference<HashMap<String, Object>>() {});
					applyLayout(layouts, basicGraph.graphModel);
				}
										
				graphJson = getSigmaGraph(basicGraph.graphModel.getDirectedGraph());
			} catch (Exception ex) {
				ex.printStackTrace();
				graphJson = "{error:" + ex.getMessage() + "}";
			}

			response.end(graphJson);

		});
	}

	/**
	 * Applying Basic Configuration
	 * 
	 * @paraam basic settings about data type etc
	 * 
	 */

	public void applyBasicConfiguration(Map<String, Object> basicSettings,ConfigurationManager configurationManager) {
		if (basicSettings.get("selectedDataSource") != null) {
			configurationManager.getConfiguration().setSelectedDataSource(basicSettings.get("selectedDataSource").toString());
		}
		if (basicSettings.get("selectedDataSource") != null&& basicSettings.get("selectedDataSource").equals("file")) {
			configurationManager.getConfiguration().setSelectedDataSource(basicSettings.get("selectedDataSource").toString());
			configurationManager.getConfiguration().getDatasource().setFilePath(basicSettings.get("filePath").toString());
		}
		if (basicSettings.get("selectedDataSource") != null&& basicSettings.get("selectedDataSource").equals("elasticsearch")) {
			configurationManager.getConfiguration().setSelectedDataSource(basicSettings.get("selectedDataSource").toString());
			configurationManager.getConfiguration().getDatasource().getElasticsearchDocument().setSearchValue(basicSettings.get("searchValue").toString().trim());
			configurationManager.getConfiguration().getDatasource().getElasticsearchDocument().setHost(basicSettings.get("host").toString().trim());
			configurationManager.getConfiguration().getDatasource().getElasticsearchDocument().setPort(Integer.parseInt(basicSettings.get("port").toString().trim()));
			configurationManager.getConfiguration().getDatasource().getElasticsearchDocument().setClusterName(basicSettings.get("clusterName").toString().trim());
			configurationManager.getConfiguration().getDatasource().getElasticsearchDocument().setIndex(basicSettings.get("index").toString().trim());
			configurationManager.getConfiguration().getDatasource().getElasticsearchDocument().setType(basicSettings.get("type").toString().trim());
			configurationManager.getConfiguration().getDatasource().getElasticsearchDocument().setDocumentsLimit(Integer.parseInt(basicSettings.get("documentsLimit").toString().trim()));
		}
		if (basicSettings.get("selectedLayout") != null) {
			configurationManager.getConfiguration().setSelectedLayout(basicSettings.get("selectedLayout").toString());

		}
	}

	/**
	 * use to apply layouts on the given graph model
	 * 
	 * @param settings
	 * @param graphModel
	 * @throws Exception
	 */
	public void applyLayout(Map<String, Object> settings, GraphModel graphModel) throws Exception {
		configurationManager.getConfiguration().setSelectedLayout(settings.get("name").toString().trim());
		if (settings.get("name").toString().trim().equals("YifanHuLayout")) {
			configurationManager.getConfiguration().getLayout().getYiFanHuLayout().setDistance((int) settings.get("distance"));
			configurationManager.getConfiguration().getLayout().getYiFanHuLayout().setIteration((int) settings.get("iteration"));
		}else if(settings.get("name").toString().trim().equals("FruchtermanReingold")){
			float area = Float.parseFloat(settings.get("area").toString().trim());
			double speed = Double.parseDouble(settings.get("speed").toString().trim());
			double gravity = Double.parseDouble(settings.get("gravity").toString().trim());
			int iteration = Integer.parseInt(settings.get("iteration").toString().trim());
			configurationManager.getConfiguration().getLayout().getFruchtermanReingold().setArea(area);
			configurationManager.getConfiguration().getLayout().getFruchtermanReingold().setSpeed(speed);
			configurationManager.getConfiguration().getLayout().getFruchtermanReingold().setGravity(gravity);
			configurationManager.getConfiguration().getLayout().getFruchtermanReingold().setIteration(iteration);
		}
    	new LayoutManager(configurationManager.getConfiguration()
				.getSelectedLayout(), graphModel,
				configurationManager.getConfiguration());

	}
	/**
	 * This method is use to apply filters
	 * @param settings
	 */
	public void applyFilters(Map<String, Object> settings, Graph graph){
		
		attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		if(settings.containsKey("pageRankThreashhold")){
			filterImplementation.applyPageRank(graph, attributeModel, graphFilter,Double.parseDouble( settings.get("pageRankThreashhold").toString()), "pageranks");
		}
		if(settings.containsKey("nodeCentrailityThreashhold")){
			filterImplementation.applyNodeCentralityThreashhold(graph, attributeModel, graphFilter,  Double.parseDouble(settings.get("nodeCentrailityThreashhold").toString()), "Betweenness Centrality");
		}
		if(settings.containsKey("neighborRangeThreashhold")){
			filterImplementation.applyNeighborcountThreshHold(graph, attributeModel, graphFilter, Double.parseDouble(settings.get("neighborRangeThreashhold").toString()), "NeighborCount");
		}
		graphPreview.rankingColorByDegree(rankingController);
		
	}

	/**
	 * use to get basic gephi graph
	 * 
	 * @return
	 * @throws Exception
	 */
	public BasicGraph getBasicgraph() throws Exception {
		return new BasicGraph(configurationManager.getConfiguration());
	}

	/**
	 * use to generate sigmagraph from given graph
	 * 
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
