package org.projectspinoza.gsakkit.layouts;



import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.ranking.api.RankingController;
import org.projectspinoza.gsakkit.util.Configurations;
import org.projectspinoza.gsakkit.util.GraphFilter;

import de.uni_leipzig.informatik.asv.gephi.chinesewhispers.ChineseWhispersClusterer;

public class LayoutImplementation {

	public void setLayout(LayoutList layout,GraphModel graphModel,Configurations conf){
		switch(layout){
		case YIfanHuLayout:
			YIfanHuLayout(graphModel, conf);
		break;
		case ForceAtlasLayout:
			
			ForceAtlasLayout(graphModel, conf);
			break;
		case FruchtermanReingold:
			
			FruchtermanReingold(graphModel, conf);
			break;
		
		}
	}
	public void YIfanHuLayout(GraphModel graphModel,Configurations conf){
		
        YifanHuLayout layout = new YifanHuLayout(null,new StepDisplacement(1f));
		
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
	     layout.setOptimalDistance( conf.getLayout().getYifanHuLayout().getDistance());
		layout.initAlgo();
		
		for (int i = 0; i < conf.getLayout().getYifanHuLayout().getIteration() && layout.canAlgo(); i++) {
			layout.goAlgo();
		}
		
		layout.endAlgo();
	}
	public void ForceAtlasLayout (GraphModel graphModel, Configurations conf){
		ForceAtlasLayout layout = new ForceAtlasLayout(null);
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();

		layout.setSpeed(conf.getLayout().getForceAtlasLayout().getSpeed());
		layout.setConverged(conf.getLayout().getForceAtlasLayout().isCovered());
		layout.setInertia(conf.getLayout().getForceAtlasLayout().getInteria());
		layout.setGravity(conf.getLayout().getForceAtlasLayout().getGravity());
		layout.setMaxDisplacement(conf.getLayout().getForceAtlasLayout().getMaxDisplacement());

		layout.initAlgo();
		for (int i = 0; i < conf.getLayout().getForceAtlasLayout().getIteration() && layout.canAlgo(); i++) {
			layout.goAlgo();
		}
		layout.endAlgo();
	}
	
	public void FruchtermanReingold(GraphModel graphModel, Configurations conf){
		FruchtermanReingold layout = new FruchtermanReingold(null);
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();

		layout.setArea(conf.getLayout().getFruchtermanReingold().getArea());
		layout.setSpeed(conf.getLayout().getFruchtermanReingold().getSpeed());
		layout.setGravity(conf.getLayout().getFruchtermanReingold().getGravity());

		layout.initAlgo();
		
		for (int i = 0; i <conf.getLayout().getFruchtermanReingold().getIteration() && layout.canAlgo(); i++) {
			layout.goAlgo();
		}
		
		layout.endAlgo();
	}
	
	public void setNodeSizeBy(String nodeSizeBy,AttributeModel attributeModel,RankingController rankingController,GraphFilter graphFilter ){
		if (nodeSizeBy.equals("nc")) {
			graphFilter.rankSize("Betweenness Centrality", attributeModel,rankingController);
		} else if (nodeSizeBy.equals("pr")) {
			graphFilter.rankSize("pageranks", attributeModel, rankingController);
		}
	}
	public void chineseWhispersClusterer(GraphModel graphModel){
		ChineseWhispersClusterer cwc = new ChineseWhispersClusterer();
		cwc.execute(graphModel);
	}
	
}
