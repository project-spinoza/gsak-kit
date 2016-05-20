package org.projectspinoza.gephikit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.openide.util.Lookup;
import org.projectspinoza.gephikit.configuration.Configuration;
import org.projectspinoza.gephikit.datasource.DataLoader;
import org.projectspinoza.gephikit.datasource.ElasticSearchDataLoader;
import org.projectspinoza.gephikit.datasource.ElasticSearchDocuments;
import org.projectspinoza.gephikit.datasource.FileLoader;
import org.projectspinoza.gephikit.filters.GraphPreview;

public class BasicGraph {
	ProjectController pc;
	Workspace workSpace;
	GraphModel graphModel;
	Configuration configuration;
	DataLoader dataloader;
	private Container container;
	private Workspace workspace;
	private ImportController importController;
	PreviewModel previewModel;
	GraphPreview graphPreview;
	RankingController rankingController;
	Configuration conf;
	public BasicGraph(Configuration config) throws Exception {
		initialize(config);
		processDataSource();

	}


	public void initialize(Configuration config) {
		configuration = config;
		pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		workSpace = pc.getCurrentWorkspace();
		graphModel = Lookup.getDefault().lookup(GraphController.class)
				.getModel();
		workspace = pc.getCurrentWorkspace();
		importController = Lookup.getDefault().lookup(ImportController.class);
		previewModel = Lookup.getDefault().lookup(PreviewController.class)
                .getModel();
		graphPreview = new GraphPreview(); 
		rankingController = Lookup.getDefault().lookup(RankingController.class);
		conf = config;
	}
	
    /**
     * use to process data from different data sources
     * @throws Exception
     */
	public void processDataSource() throws Exception {
		
		if (configuration.getSelectedDataSource().trim().equals("file")) {
			
			dataloader = new FileLoader(configuration.getDatasource().getFilePath());
			container = dataloader.load();
		}else if(configuration.getSelectedDataSource().trim().equals("elasticsearch")){
			
			String host = configuration.getDatasource().getElasticsearchDocument().getHost();
			int port = configuration.getDatasource().getElasticsearchDocument().getPort();
			String clusterName = configuration.getDatasource().getElasticsearchDocument().getClusterName();
			String index = configuration.getDatasource().getElasticsearchDocument().getIndex();
			String type = configuration.getDatasource().getElasticsearchDocument().getType();
			List<String> searchFields = configuration.getDatasource().getElasticsearchDocument().getSearchFields();
			String searchValue = configuration.getDatasource().getElasticsearchDocument().getSearchValue();
			List<String> returnFields = configuration.getDatasource().getElasticsearchDocument().getReturnFields();
			int documentsLimit = configuration.getDatasource().getElasticsearchDocument().getDocumentsLimit();
			
			ElasticSearchDocuments esd = new ElasticSearchDocuments(host, port, clusterName, index, type, searchFields, searchValue, returnFields, documentsLimit);
			dataloader = new ElasticSearchDataLoader(esd.getDocuments(), configuration.getDatasource().getElasticsearchDataLoad().getFields(), conf);
			container = dataloader.load();
			
		}
		importController.process(container, new DefaultProcessor(), workspace);
	   
         
	}

	public GraphModel getGraphModel() {
		return graphModel;
	}


}
