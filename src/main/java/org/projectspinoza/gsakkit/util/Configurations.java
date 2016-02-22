package org.projectspinoza.gsakkit.util;

import org.projectspinoza.gsakkit.layouts.LayoutSettings;

public class Configurations {
	LayoutSettings layout;
	FilterSetting filter;
	String selectedLayout;
	DataSourceSettings datasource;
	public String getSelectedLayout() {
		return selectedLayout;
	}

	public void setSelectedLayout(String selectedLayout) {
		this.selectedLayout = selectedLayout;
	}

	public LayoutSettings getLayout() {
		return layout;
	}	

	public void setLayout(LayoutSettings layout) {
		this.layout = layout;
	}

	public FilterSetting getFilter() {
		return filter;
	}

	public void setFilter(FilterSetting filter) {
		this.filter = filter;
	}

	public DataSourceSettings getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSourceSettings datasource) {
		this.datasource = datasource;
	}

	
}
