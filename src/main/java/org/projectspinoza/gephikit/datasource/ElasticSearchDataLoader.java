package org.projectspinoza.gephikit.datasource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.EdgeDraft.EdgeType;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.io.importer.impl.ImportContainerImpl;
import org.projectspinoza.gephikit.configuration.Configuration;

public class ElasticSearchDataLoader implements DataLoader {
	public List<Map<String, Object>> lines;
	ImportContainerImpl container;
	List<String[]> edges;
	List<Map<String, Object>> fields;
	Configuration conf;

	public ElasticSearchDataLoader() {
		// TODO Auto-generated constructor stub
	}

	public ElasticSearchDataLoader(List<Map<String, Object>> documents,
			List<Map<String, Object>> attributes, Configuration conf) {
	
		initialize(conf);
		startProcess(documents, attributes);
	}

	public void initialize(Configuration conf) {
		container = new ImportContainerImpl();
		edges = new ArrayList<String[]>();
		lines = new ArrayList<Map<String, Object>>();
		this.conf = conf;
	}

	public void startProcess(List<Map<String, Object>> documents,
			List<Map<String, Object>> attributes) {

		for (Map<String, Object> document : documents) {
			
			for (Map<String, Object> attribute : attributes) {
				if (attribute.get("name") == null || attribute.get("splitBy") == null) {
					continue;
				}
				Map<String, Object> documentMap = new HashMap<String, Object>();
				documentMap.put("text",document.get(attribute.get("name").toString()));
				documentMap.put("splitBy", attribute.get("splitBy").toString());
				lines.add(documentMap);			
			}
		}
	}

	public List<Map<String, Object>> getFields() {
		return fields;
	}

	public void setFields(List<Map<String, Object>> fields) {
		this.fields = fields;
	}

	public Container load() {
		for (Map<String, Object> line : lines) {
			String[] splitTags = line.get("text").toString().trim().split(line.get("splitBy").toString());
			if(splitTags!=null){
			    if(splitTags.length > conf.getTagLimit()){
			        splitTags =   Arrays.copyOfRange(splitTags, 0, conf.getTagLimit());  
			    }
    			List<String[]> edges = buildEdges(splitTags);
    			addToContainer(edges);
    		}
		}

		return container;
	}

	private void addToContainer(List<String[]> edges) {

		for (String[] edge : edges) {
            if(edge[0].trim().isEmpty() || edge[1].trim().isEmpty())
            	continue;
			EdgeDraft edgeDraft = null;
			String edgeId = edge[0] + "-" + edge[1];

			if (container.edgeExists(edgeId)) {
				edgeDraft = container.getEdge(edgeId);

				float weight = edgeDraft.getWeight();
				weight += 1;
				edgeDraft.setWeight(weight);
				continue;
			}

			edgeDraft = container.factory().newEdgeDraft();
			edgeDraft.setId(edgeId);
			edgeDraft.setWeight(1f);
			edgeDraft.setType(EdgeType.DIRECTED);

			edgeDraft.setLabel("CO_HASHTAG");

			NodeDraft source = getOrCreateNodeDraft(edge[0], edge[0]);
			NodeDraft target = getOrCreateNodeDraft(edge[1], edge[1]);

			edgeDraft.setSource(source);
			edgeDraft.setTarget(target);

			container.addEdge(edgeDraft);
		}
	}

	private List<String[]> buildEdges(String[] splitTags) {
		List<String[]> edges = new ArrayList<String[]>();
		List<String> taglist = new ArrayList<String>();
     
		for (String splittag : splitTags) {
			if (splittag.trim().isEmpty() || splittag.length() > 25)
				continue;
			taglist.add(splittag);
		}
		for (int i = 0; i < taglist.size() - 1; i++) {
			for (int j = i + 1; j < taglist.size(); j++) {
				String edge[] = new String[2];
				edge[0] = taglist.get(i);
				edge[1] = taglist.get(j);
				edges.add(edge);
			}
		}

		return edges;
	}

	private NodeDraft getOrCreateNodeDraft(String id, String label) {

		NodeDraft nodeDraft = null;
		if (id == null) {

			System.exit(0);
		}

		if (container.nodeExists(id)) {
			nodeDraft = container.getNode(id);
		} else {
			nodeDraft = container.factory().newNodeDraft();
			nodeDraft.setId(id);
			nodeDraft.setLabel(label);

			container.addNode(nodeDraft);
		}

		return nodeDraft;
	}

}
