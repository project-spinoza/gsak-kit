package org.projectspinoza.gephikit.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.gephi.io.importer.impl.ImportContainerImpl;


public class ElasticSearchDocuments {
        String host;
        int port;
        String clusterName;
        String index;
        String type;
        List<String> searchFields;
        String searchValue;
        int documentsLimit;
        List<String> returnFields;
        Client client;
    	ImportContainerImpl container;
    	List<Map<String, Object>> documents;
    	TransportClient transportClient;
    	private static org.apache.log4j.Logger log = Logger.getLogger(ElasticSearchDocuments.class);
    	ElasticSearchDocuments(){
    		
    	}
        public ElasticSearchDocuments(String host, int port,String clusterName, String index, String type, List<String> searchFields, String searchValue,List<String> returnFields, int documentsLimit) throws Exception{
        	
        	this.host= host;
        	this.port = port;
        	this.clusterName = clusterName;
        	this.index = index;
        	this.type = type;
        	this.searchFields = searchFields;
        	this.searchValue = searchValue;
        	this.documentsLimit = documentsLimit;
        	this.returnFields = returnFields;
        	
        	this.startProcess();
        }
        public void startProcess() throws Exception{
        	
        	if(getClient() != null){
        		documents = getDocuments(client,  index, type, searchValue, searchFields, returnFields, documentsLimit );
        	}else{
        		log.error("ElasticSearch Connectivity Fails");
        	}
        }
        public Client getClient() throws NoNodeAvailableException{
        	Settings clientSettings;
        	if(client == null){
        	if(!clusterName.trim().isEmpty()){
        		clientSettings = ImmutableSettings.settingsBuilder().put("cluster.name",clusterName).build();
        	} else {
        		clientSettings = ImmutableSettings.settingsBuilder().put("cluster.name","elasticsearch").build();
        	}
        
        	client = new TransportClient(clientSettings).addTransportAddress(new InetSocketTransportAddress(host,port));
        	}
        	return client;
        	
        }
       
        public List<Map<String, Object>> getDocuments(Client client,String index, String type, String searchValue,List<String> searchFields,List<String> returnFields ,int documentsLimit) throws Exception{
        	List<Map<String, Object> > responseList = new ArrayList<Map<String, Object>>();
            QueryStringQueryBuilder queryString = QueryBuilders.queryString(searchValue);
            for(String searchField : searchFields){
            	queryString.field(searchField);
            }
            
    		SearchResponse response = client
    				.prepareSearch(index).setTypes(type)
    				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
    				.setQuery(queryString)
    				.setFrom(0)
    				.setSize(documentsLimit)
    				.setExplain(true)
    				.execute()
    				.actionGet();

    		for (SearchHit hit : response.getHits()) {
    			Map<String, Object> responseMap = new HashMap<String, Object>();
    			for(String field : returnFields){
    				responseMap.put(field, hit.getSource().get(field));	
    			}
    			responseList.add(responseMap);
    		}
    		return responseList;
        	
        }
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getClusterName() {
			return clusterName;
		}
		public void setClusterName(String clusterName) {
			this.clusterName = clusterName;
		}
		public String getIndex() {
			return index;
		}
		public void setIndex(String index) {
			this.index = index;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
		public List<String> getSearchFields() {
			return searchFields;
		}
		public void setSearchFields(List<String> searchFields) {
			this.searchFields = searchFields;
		}
		public List<String> getReturnFields() {
			return returnFields;
		}
		public void setReturnFields(List<String> returnFields) {
			this.returnFields = returnFields;
		}
		public void setClient(Client client) {
			this.client = client;
		}
		public String getSearchValue() {
			return searchValue;
		}
		public void setSearchValue(String searchValue) {
			this.searchValue = searchValue;
		}
		public int getDocumentsLimit() {
			return documentsLimit;
		}
		public void setDocumentsLimit(int documentsLimit) {
			this.documentsLimit = documentsLimit;
		}
		
		public List<Map<String, Object>> getDocuments() {
			return documents;
		}
		public void setDocuments(List<Map<String, Object>> documents) {
			this.documents = documents;
		}
		
        
        
}
