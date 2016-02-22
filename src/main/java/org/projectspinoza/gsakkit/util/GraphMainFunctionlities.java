package org.projectspinoza.gsakkit.util;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.PageRank;

public class GraphMainFunctionlities {

	public void getCentraility(GraphModel graphModel, AttributeModel attributeModel, GraphDistance graphDistance, boolean directed){

		graphDistance.setDirected(directed);
		graphDistance.execute(graphModel, attributeModel);
	}
	
   public void calculatePageRank(GraphModel graphModel, AttributeModel attributeModel,boolean directed){
	   PageRank pr = new PageRank();
		pr.setDirected(directed);
		pr.setEpsilon(0.001);
		pr.setProbability(0.85);
		pr.execute(graphModel, attributeModel);
   }	
   
  public Graph addColumnAttributeModel(Graph graph, AttributeModel attributeModel, String columnName){
	  attributeModel.getNodeTable().addColumn(columnName,
				AttributeType.DOUBLE);
		Node[] node1 = graph.getNodes().toArray();
		double neighborcount = 0.0;
		for (int i = 0; i < node1.length; i++) {
			Node[] neighbors = graph.getNeighbors(node1[i]).toArray();
			neighborcount = neighbors.length;
			node1[i].getAttributes().setValue(columnName, neighborcount);
		}
		return graph;
  } 
	
}
