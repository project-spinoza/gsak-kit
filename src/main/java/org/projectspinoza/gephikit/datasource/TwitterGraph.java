package org.projectspinoza.gephikit.datasource;

import java.util.HashSet;

import org.gephi.graph.api.Graph;


import org.projectspinoza.gephikit.configuration.Configuration;

import uk.ac.ox.oii.sigmaexporter.model.GraphElement;

public interface TwitterGraph {

	public void build(Graph graph,Configuration conf);

	public HashSet<GraphElement> getNodes();

	public HashSet<GraphElement> getEdges();

}