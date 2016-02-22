package org.projectspinoza.gsakkit;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.server.handlers.form.FormParserFactory.Builder;
import io.undertow.util.Headers;

import java.io.File;

import org.codehaus.jackson.map.ObjectMapper;
import org.gephi.graph.api.Graph;
import org.projectspinoza.gsakkit.datasources.ElasticSearchLoader;
import org.projectspinoza.gsakkit.datasources.FileLoader;
import org.projectspinoza.gsakkit.util.Configurations;
import org.projectspinoza.gsakkit.util.SigmaGraph;

/**
 * Hello world!
 *
 */
public class App {
	private static final String GRAPH = "/gephi";
	private static String CONFIG_FILE;
	public static void main(String[] args) {
		if(args.length == 0){
			System.err.println("Configuration file path not set!");
			System.exit(0);
		}
		CONFIG_FILE = args[0];
		

		Undertow server = Undertow
				.builder()
				.addHttpListener(8182, "localhost")
				.setHandler(
						Handlers.routing().post("/gephi", getGraphHandler()))
				.build();
		server.start();
	}

	private static PathHandler getGraphHandler() {
		PathHandler graphPathHandler = Handlers.path(Handlers.redirect(GRAPH))
				.addPrefixPath(GRAPH, new HttpHandler() {

					public void handleRequest(final HttpServerExchange exchange)
							throws Exception {
						exchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
								"text/plain");
						BasicGraph basicGraph = null;
						Builder builder = FormParserFactory.builder();
						final FormDataParser formDataParser = builder.build()
								.createParser(exchange);

						ObjectMapper mapper = new ObjectMapper();
						Configurations conf = mapper.readValue(new File(CONFIG_FILE), Configurations.class);
						if (formDataParser != null) {
							exchange.startBlocking();
							FormData formData = formDataParser.parseBlocking();

							for (String data : formData) {

								if (data.equals("neighborRange")) {
									conf.getFilter().setNeighborRange(
											Integer.parseInt(formData.get(data)
													.getFirst().getValue()));
								} else if (data.equals("nodeSizeBy")) {
									conf.getLayout().setNodeSizeBy(
											formData.get(data).getFirst()
													.getValue().toString());
								} else if (data
										.equals("nodeCentrailityThreashhold")) {
									conf.getFilter()
											.setNodeCentrailityThreashhold(
													Integer.parseInt(formData
															.get(data)
															.getFirst()
															.getValue()));

								} else if (data.equals("pageRankThreashhold")) {
									conf.getFilter().setPageRankThreashhold(
											Integer.parseInt(formData.get(data)
													.getFirst().getValue()));
								} else if (data.equals("edgeColorBy")) {
									conf.getFilter().setEdgeColorBy(
											formData.get(data).getFirst()
													.getValue().toString());
								} else if (data.equals("edgeTypeBy")) {
									conf.getFilter().setEdgeTypeBy(
											formData.get(data).getFirst()
													.getValue().toString());
								} else if (data.equals("backgroundColor")) {
									conf.getFilter().setBackgroundColor(
											formData.get(data).getFirst()
													.getValue().toString());
								} else if (data.equals("selectedLayout")) {

									conf.setSelectedLayout(formData.get(data)
											.getFirst().getValue().toString());
								}

							}
						}
						if (conf.getDatasource().getSource().trim()
								.equals("file")) {

							FileLoader fileLoader = new FileLoader(conf
									.getDatasource().getFilePath());
							basicGraph = new BasicGraph(fileLoader, conf);
						}else if(conf.getDatasource().getSource().trim().equals("elasticsearch")){
							ElasticSearchLoader elasticsearchLoader = new ElasticSearchLoader(conf);
							basicGraph = new BasicGraph(elasticsearchLoader, conf);
						}
						Graph graph = basicGraph.getGraph();
						SigmaGraph sigmaGraph = new SigmaGraph();
						sigmaGraph.build(graph, conf);
						exchange.getResponseSender().send(
								mapper.writeValueAsString(sigmaGraph));
					}
				});

		return graphPathHandler;
	}
}
