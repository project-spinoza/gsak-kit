package org.projectspinoza.gsakkit.datasources;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.EdgeDraft.EdgeType;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.io.importer.impl.ImportContainerImpl;
import org.projectspinoza.gsakkit.util.Configurations;

public class ElasticSearchLoader implements DataLoader {
	Configurations config;
	Client client;
	ImportContainerImpl container;
	List<String> tweets;
	TransportClient transportClient;

	public ElasticSearchLoader(Configurations conf) throws UnknownHostException {
		// TODO Auto-generated constructor stub
		this.config = conf;
		this.initialize();
		this.startProcess();
		
	}
	public void startProcess() throws UnknownHostException{
		client = getClient();
		if (client != null) {

			tweets = elasticsearchSearch(config.getDatasource()
					.getElasticsearch().getIndexName(), config.getDatasource()
					.getElasticsearch().getIndexType(), config.getDatasource()
					.getElasticsearch().getSearchValue());
			

		}
	}

	public void initialize() {
		container = new ImportContainerImpl();
		tweets = new ArrayList<String>();
		client = null;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() throws UnknownHostException {
		Settings clientSettings;
		if(!config.getDatasource().getElasticsearch().getClusterName().isEmpty()){
			
			clientSettings = ImmutableSettings
					.settingsBuilder()
					.put("cluster.name",config.getDatasource().getElasticsearch().getClusterName()).build();
		}else{
			clientSettings = ImmutableSettings
					.settingsBuilder()
					.put("cluster.name","elasticsearch").build();
		}
		client = new TransportClient(clientSettings)
		 .addTransportAddress(new InetSocketTransportAddress(config.getDatasource().getElasticsearch().getHost(),config.getDatasource().getElasticsearch().getPort()));
		return client;
	}

	public List<String> elasticsearchSearch(String indexName, String indexType,
			String searchValue) throws UnknownHostException {
		List<String> responseList = new ArrayList<String>();

		SearchResponse response = client.prepareSearch(indexName)
				.setTypes(indexType)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.queryString( config.getDatasource().getElasticsearch().getSearchValue()))
				.setFrom(0).setSize(500000).setExplain(true).execute().actionGet();

		for (SearchHit hit : response.getHits()) {
			responseList.add(hit.getSource().get(config.getDatasource().getElasticsearch().getSearchField()).toString());
		}
		
		return responseList;
	}

	public Container load() {
		for (String tweet : tweets) {
			List<String[]> edges = buildEdges(tweet);
			addToContainer(edges);
		}
		return container;
	}

	public ImportContainerImpl getContainer() {
		return container;
	}

	private void addToContainer(List<String[]> edges) {

		for (String[] edge : edges) {

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
			// edgeDraft.setType(EdgeType.UNDIRECTED);
			edgeDraft.setLabel("CO_HASHTAG");

			NodeDraft source = getOrCreateNodeDraft(edge[0], edge[0]);
			NodeDraft target = getOrCreateNodeDraft(edge[1], edge[1]);

			edgeDraft.setSource(source);
			edgeDraft.setTarget(target);

			container.addEdge(edgeDraft);
		}
	}

	/**
	 * creates or returns nodeDraft with the id @id and label @label
	 * 
	 * @param id
	 * @param label
	 * @return NodeDraft
	 */
	private NodeDraft getOrCreateNodeDraft(String id, String label) {

		NodeDraft nodeDraft = null;

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

	private List<String[]> buildEdges(String tweet) {

		List<String[]> edges = new ArrayList<String[]>();

		String parts[] = tweet.replaceAll("/[^A-Za-z0-9 #]/", " ")
				.replace("\n", " ").replace("\r", " ")
				.replaceAll("\\P{Print}", " ").split(" ");

		Set<String> tags = new HashSet<String>();

		for (String part : parts) {
			part = part.trim();
			if (part.length() < 2 || part.length() > 20)
				continue;
			if (part.equals("#rt"))
				continue;

			if (part.startsWith("#")) {
				// . splits hashtags of type: #tag1#tag2...
				if ((part.length() - part.replace("#", "").length()) > 1) {
					String[] subParts = part.split("#");
					for (String sb : subParts) {
						sb = sb.replaceAll("[^a-zA-Z0-9_-]", "").trim();
						sb = sb.replace("\\s", "");
						if (sb.length() > 1) {
							tags.add(sb);
						}
					}
					continue;
				}
				part = part.replaceAll("[^a-zA-Z0-9_-]", "").trim();
				part = part.replace("\\s", "");
				if (part.length() > 1) {
					tags.add(part);
				}
			}
		}

		List<String> taglist = new ArrayList<String>();
		taglist.addAll(tags);
		Collections.sort(taglist);
		if (taglist.size() < 2)
			return edges;

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

}
