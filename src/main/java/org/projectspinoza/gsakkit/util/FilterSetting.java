package org.projectspinoza.gsakkit.util;

public class FilterSetting {
	int nodeCentrailityThreashhold;
	int pageRankThreashhold;
	int neighborRange;
	String nodeCentrailityBy;
	
	String edgeColorBy;
	String edgeTypeBy;
	String backgroundColor;

	public int getNodeCentrailityThreashhold() {
		return nodeCentrailityThreashhold;
	}

	public void setNodeCentrailityThreashhold(int nodeCentrailityThreashhold) {
		this.nodeCentrailityThreashhold = nodeCentrailityThreashhold;
	}

	public int getPageRankThreashhold() {
		return pageRankThreashhold;
	}

	public void setPageRankThreashhold(int pageRankThreashhold) {
		this.pageRankThreashhold = pageRankThreashhold;
	}

	public int getNeighborRange() {
		return neighborRange;
	}

	public void setNeighborRange(int neighborRange) {
		this.neighborRange = neighborRange;
	}

	public String getNodeCentrailityBy() {
		return nodeCentrailityBy;
	}

	public void setNodeCentrailityBy(String nodeCentrailityBy) {
		this.nodeCentrailityBy = nodeCentrailityBy;
	}

	

	public String getEdgeColorBy() {
		return edgeColorBy;
	}

	public void setEdgeColorBy(String edgeColorBy) {
		this.edgeColorBy = edgeColorBy;
	}

	public String getEdgeTypeBy() {
		return edgeTypeBy;
	}

	public void setEdgeTypeBy(String edgeTypeBy) {
		this.edgeTypeBy = edgeTypeBy;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
