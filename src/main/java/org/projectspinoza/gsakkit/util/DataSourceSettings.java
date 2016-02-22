package org.projectspinoza.gsakkit.util;

public class DataSourceSettings {
    String source;
    ElasticsearchSetting elasticsearch;
    String filePath;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public ElasticsearchSetting getElasticsearch() {
		return elasticsearch;
	}
	public void setElasticsearch(ElasticsearchSetting elasticsearch) {
		this.elasticsearch = elasticsearch;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
