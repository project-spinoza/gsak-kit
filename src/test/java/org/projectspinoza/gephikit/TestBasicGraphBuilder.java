package org.projectspinoza.gephikit;
import static org.junit.Assert.*;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.statistics.plugin.GraphDistance;
import org.junit.Before;
import org.junit.Test;
import org.openide.util.Lookup;
import org.projectspinoza.gephikit.configuration.ConfigurationManager;
import org.projectspinoza.gephikit.filters.GraphFilter;

public class TestBasicGraphBuilder {

	ConfigurationManager configurationManager;
	String configurationFilePath;
	BasicGraph basicGraph;
	 AttributeModel attributeModel;
	  GraphFilter graphFilter;
	@Before
	public void setup() throws Exception{
		configurationManager = new ConfigurationManager();
		configurationFilePath = "configuration.json";
		configurationManager.setInitialConfiguration(configurationFilePath);
		configurationManager.getConfiguration().setSelectedDataSource("file");
		configurationManager.getConfiguration().getDatasource().setFilePath("tweets.txt");
		basicGraph = new BasicGraph(configurationManager.getConfiguration());
		graphFilter = new GraphFilter();
		attributeModel = Lookup.getDefault()
                .lookup(AttributeController.class).getModel();
		
	} 
//	
	@Test
	public void TestTotalNodes(){		
		int expected = 49;
		int result = basicGraph.getGraphModel().getDirectedGraph().getNodeCount();
		assertEquals(expected, result);

	}
	@Test
	public void TestTotalEdges(){
		
		int expected = 135;
		int result = basicGraph.getGraphModel().getDirectedGraph().getEdgeCount();
		assertEquals(expected, result);		
		}

	@Test
	public void TestPageranksFilter(){
		
	    Graph testGraph; //. depends on graphModel
		int expected = 44;
		String columnname = "pageranks";
		AttributeColumn column = attributeModel.getNodeTable().getColumn(columnname);
		double prThreshHold = (double) 10 / 100;
		testGraph = graphFilter.removePercentageNodes(basicGraph.getGraphModel().getGraph(), column, prThreshHold ,columnname);
		int result = testGraph.getNodeCount();	
		assertEquals(expected, result);
	}
	@Test
	@SuppressWarnings("unused")
	public void TestBetweennessCentrality(){
		
		Graph testGraph ;
		int expected = 44;
		String columnname = "Betweenness Centrality";
		AttributeColumn column = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
		testGraph = graphFilter.removePercentageNodes(basicGraph.getGraphModel().getGraph(), column,(double) 10/100, columnname);
		int result = testGraph.getNodeCount();
		assertEquals(expected, result);
	}
	
	@Test
	public void TestNeighbourCount(){
		
		int expected = 49;
		Graph testGraph;
		String columnname = "NeighborCount";
		AttributeColumn column = attributeModel.getNodeTable().getColumn(columnname);
		testGraph = graphFilter.removePercentageNodes(basicGraph.getGraphModel().getGraph(), column, 10/100, columnname);
	    int result = testGraph.getNodeCount();
	    assertEquals(expected, result);
	}
	
}