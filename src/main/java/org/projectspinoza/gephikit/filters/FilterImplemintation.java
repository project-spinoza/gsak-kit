package org.projectspinoza.gephikit.filters;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.statistics.plugin.GraphDistance;

public class FilterImplemintation {

	public void applyPageRank(Graph graph, AttributeModel attributeModel,
			GraphFilter graphFilter, double pageRankThreashhold,
			String columnName) {

		AttributeColumn column = attributeModel.getNodeTable().getColumn(
				columnName);
		double prThreshHold = pageRankThreashhold / 100;
		graph = graphFilter.removePercentageNodes(graph, column, prThreshHold,
				columnName);
	}

	public void applyNodeCentralityThreashhold(Graph graph,
			AttributeModel attributeModel, GraphFilter graphFilter,
			double nodeCentrailityThreashhold, String columnName) {

		AttributeColumn column = attributeModel.getNodeTable().getColumn(
				GraphDistance.BETWEENNESS);
		graph = graphFilter.removePercentageNodes(graph, column,
				nodeCentrailityThreashhold / 100, columnName);
	}

	public void applyNeighborcountThreshHold(Graph graph,
			AttributeModel attributeModel, GraphFilter graphFilter,
			double neighborcountThreshHold, String columnName) {

		AttributeColumn column = attributeModel.getNodeTable().getColumn(
				columnName);
		graph = graphFilter.removePercentageNodes(graph, column,
				neighborcountThreshHold / 100, columnName);
	}

}
