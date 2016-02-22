package org.projectspinoza.gsakkit;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.projectspinoza.gsakkit.util.FilterImplementation;
import org.projectspinoza.gsakkit.util.GraphFilter;

public class unittesting {
@Mock
FilterImplementation filterImplimentation;
Graph graph;
AttributeModel attributeModel;
GraphFilter graphFilter;

@Before
public void setup(){
	filterImplimentation = Mockito.mock(FilterImplementation.class);
	graph = Mockito.mock(Graph.class);
	attributeModel = Mockito.mock(AttributeModel.class);
	graphFilter = Mockito.mock(GraphFilter.class);
}
@Test
public void testapplyPageRank(){
	Mockito.doNothing().when(filterImplimentation).applyPageRank(graph, attributeModel, graphFilter, 1, "pagerank");
	filterImplimentation.applyPageRank(graph, attributeModel, graphFilter, 1, "pagerank");
	Mockito.verify(filterImplimentation).applyPageRank(graph, attributeModel, graphFilter, 1, "pagerank");
}
@Test
public void testapplyNodeCentralityThreashhold(){
	Mockito.doNothing().when(filterImplimentation).applyNodeCentralityThreashhold(graph, attributeModel, graphFilter, 1, "nodeCentrailityThreashHold");
	filterImplimentation.applyNodeCentralityThreashhold(graph, attributeModel, graphFilter, 1, "nodeCentrailityThreashHold");
	Mockito.verify(filterImplimentation).applyNodeCentralityThreashhold(graph, attributeModel, graphFilter, 1, "nodeCentrailityThreashHold");
}
@Test
public void testapplyNeighborcountThreshHold(){
	Mockito.doNothing().when(filterImplimentation).applyNeighborcountThreshHold(graph, attributeModel, graphFilter, 1, "NeighborcountThreshHold");
	filterImplimentation.applyNeighborcountThreshHold(graph, attributeModel, graphFilter, 1, "NeighborcountThreshHold");
	Mockito.verify(filterImplimentation).applyNeighborcountThreshHold(graph, attributeModel, graphFilter, 1, "NeighborcountThreshHold");
}


}
