package org.projectspinoza.gsakkit.util;

import java.util.HashSet;

import org.gephi.graph.api.Graph;

import uk.ac.ox.oii.sigmaexporter.model.GraphElement;

public interface TwitterGraph {

	public void build(Graph graph,Configurations conf);

	public HashSet<GraphElement> getNodes();

	public HashSet<GraphElement> getEdges();

}