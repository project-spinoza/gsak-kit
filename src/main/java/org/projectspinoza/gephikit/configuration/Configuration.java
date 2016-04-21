package org.projectspinoza.gephikit.configuration;

import org.projectspinoza.gephikit.datasource.DataSource;
import org.projectspinoza.gephikit.layouts.Layout;



public class Configuration {
	int port;
	String host;
	String selectedDataSource;
	Layout layout;
	DataSource datasource;
	String selectedLayout;
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public String getSelectedLayout() {
		return selectedLayout;
	}

	public void setSelectedLayout(String selectedLayout) {
		this.selectedLayout = selectedLayout;
	}

	public String getSelectedDataSource() {
		return selectedDataSource;
	}

	public void setSelectedDataSource(String selectedDataSource) {
		this.selectedDataSource = selectedDataSource;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	
}
