package org.projectspinoza.gephikit.layouts;

import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.projectspinoza.gephikit.configuration.Configuration;


public class LayoutManager {

	public LayoutManager(String layoutName, GraphModel graphModel,
			Configuration configuration) {
		initProcess(layoutName, graphModel, configuration);
	}

	/**
	 * This function is use to apply appropriate layout
	 * 
	 * @param layoutName
	 * @param graphModel
	 * @param configuration
	 */
	public void initProcess(String layoutName, GraphModel graphModel,
			Configuration configuration) {
		if (layoutName.toString().trim().equals("YifanHuLayout")) {

			YiFanLayOut(graphModel, configuration.getLayout()
					.getYiFanHuLayout().getDistance(), configuration
					.getLayout().getYiFanHuLayout().getIteration());
			
		} else if (layoutName.toString().trim().equals("FruchtermanReingold")) {
			float area = configuration.getLayout().getFruchtermanReingold()
					.getArea();
			double speed = configuration.getLayout().getFruchtermanReingold()
					.getSpeed();
			double gravity = configuration.getLayout().getFruchtermanReingold()
					.getGravity();
			int iteration = configuration.getLayout().getFruchtermanReingold()
					.getIteration();
			FruchtermanReingold(graphModel, area, speed, gravity, iteration);
		}
	}

	/**
	 * Applying YiFanLayOut
	 * 
	 * @param graphModel
	 * @param distance
	 * @param iteration
	 */

	public void YiFanLayOut(GraphModel graphModel, int distance, int iteration) {
		
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));

		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setOptimalDistance((float) distance);
		layout.initAlgo();

		for (int i = 0; i < iteration && layout.canAlgo(); i++) {
			layout.goAlgo();
		}

		layout.endAlgo();
		
	}

	/**
	 * Applying FruchtermanReingold
	 * 
	 * @param graphModel
	 * @param conf
	 */

	public void FruchtermanReingold(GraphModel graphModel, float area,
			double speed, double gravity, int iteration) {
		FruchtermanReingold layout = new FruchtermanReingold(null);
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();

		layout.setArea(area);
		layout.setSpeed(speed);
		layout.setGravity(gravity);

		layout.initAlgo();

		for (int i = 0; i < iteration && layout.canAlgo(); i++) {
			layout.goAlgo();
		}

		layout.endAlgo();
	}
}
