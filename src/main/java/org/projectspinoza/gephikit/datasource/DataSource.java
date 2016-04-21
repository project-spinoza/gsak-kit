package org.projectspinoza.gephikit.datasource;

public class DataSource {
	String filePath;
	FileLoader fileloader;
	ElasticSearchDocuments elasticsearchDocument;
	ElasticSearchDataLoader elasticsearchDataLoad;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public FileLoader getFileloader() {
		return fileloader;
	}

	public void setFileloader(FileLoader fileloader) {
		this.fileloader = fileloader;
	}

	public ElasticSearchDocuments getElasticsearchDocument() {
		return elasticsearchDocument;
	}

	public void setElasticsearchDocument(
			ElasticSearchDocuments elasticsearchDocument) {
		this.elasticsearchDocument = elasticsearchDocument;
	}

	public ElasticSearchDataLoader getElasticsearchDataLoad() {
		return elasticsearchDataLoad;
	}

	public void setElasticsearchDataLoad(
			ElasticSearchDataLoader elasticsearchDataLoad) {
		this.elasticsearchDataLoad = elasticsearchDataLoad;
	}
	

}
