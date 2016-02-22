package org.projectspinoza.gsakkit;



import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.RankingController;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;
import org.projectspinoza.gsakkit.datasources.DataLoader;
import org.projectspinoza.gsakkit.layouts.LayoutImplementation;
import org.projectspinoza.gsakkit.layouts.LayoutList;
import org.projectspinoza.gsakkit.util.Configurations;
import org.projectspinoza.gsakkit.util.FilterImplementation;
import org.projectspinoza.gsakkit.util.GraphFilter;
import org.projectspinoza.gsakkit.util.GraphMainFunctionlities;
import org.projectspinoza.gsakkit.util.GraphPreview;

public class BasicGraph {
	DataLoader loader;
	private ProjectController projectController;
	private Workspace workspace; // . depends on pc
	private GraphModel graphModel;
	private Graph graph; // . depends on graphModel
	@SuppressWarnings("unused")
	private PreviewModel previewModel;
	private ImportController importController; // . depends on workspace
	private Container graphContainer; // . depends on importController
	AttributeModel attributeModel;
	GraphMainFunctionlities graphMainFunctionlities;
	LayoutImplementation layoutImplementation ;
	FilterImplementation filterImplementation ;
	GraphFilter graphFilter;
	RankingController rankingController;
	GraphPreview graphPreview; 
	Configurations conf;

	public BasicGraph(DataLoader loader,Configurations conf) {
		this.loader = loader;
		this.conf = conf;
		initialize();
		buildBasicGraph();
	}

	public void initialize() {
		projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		workspace = projectController.getCurrentWorkspace();
		graphModel = Lookup.getDefault().lookup(GraphController.class)
				.getModel();
		previewModel = Lookup.getDefault().lookup(PreviewController.class)
				.getModel();
		importController = Lookup.getDefault().lookup(ImportController.class);
		attributeModel = Lookup.getDefault().lookup(AttributeController.class)
				.getModel();
		graphMainFunctionlities =new GraphMainFunctionlities();
		layoutImplementation = new LayoutImplementation();
		filterImplementation = new FilterImplementation();
		graphFilter = new GraphFilter();
		rankingController = Lookup.getDefault().lookup(RankingController.class);
		graphPreview = new GraphPreview(); 
	}

	public void buildBasicGraph() {

		graphContainer = loader.load();

		importController.process(graphContainer, new DefaultProcessor(),
				workspace);
		

		graphMainFunctionlities.getCentraility(graphModel, attributeModel, new GraphDistance(), true);
		graphMainFunctionlities.calculatePageRank(graphModel, attributeModel, true);
		
		graph=graphMainFunctionlities.addColumnAttributeModel(graphModel.getDirectedGraph(), attributeModel, "NeighborCount");
		double pageRankThreashhold = conf.getFilter().getPageRankThreashhold();
		double nodeCentrailityThreashhold =  conf.getFilter().getNodeCentrailityThreashhold();
		double neighborRangeThreashhold =  conf.getFilter().getNeighborRange();
		
		if(pageRankThreashhold !=0){
			filterImplementation.applyPageRank(graph, attributeModel, graphFilter, pageRankThreashhold, "pageranks");
		}
		if(nodeCentrailityThreashhold !=0){
			filterImplementation.applyNodeCentralityThreashhold(graph, attributeModel, graphFilter, nodeCentrailityThreashhold, "Betweenness Centrality");
		}
		if(neighborRangeThreashhold !=0){
			filterImplementation.applyNeighborcountThreshHold(graph, attributeModel, graphFilter, neighborRangeThreashhold, "NeighborCount");
		}
		graphPreview.rankingColorByDegree(rankingController);
		layoutImplementation.setLayout(LayoutList.valueOf(conf.getSelectedLayout()), graphModel, conf);
		layoutImplementation.setNodeSizeBy(conf.getLayout().getNodeSizeBy(), attributeModel, rankingController, graphFilter);
		layoutImplementation.chineseWhispersClusterer(graphModel);

	}

	

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

}
