package org.projectspinoza.gsakkit.layouts;

public class ForceAtlasLayoutSetting {
	String name;

	double speed;
	boolean covered;
	double interia;
	double gravity;
	double maxDisplacement;
	int iteration;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public boolean isCovered() {
		return covered;
	}

	public void setCovered(boolean covered) {
		this.covered = covered;
	}

	public double getInteria() {
		return interia;
	}

	public void setInteria(double interia) {
		this.interia = interia;
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	public double getMaxDisplacement() {
		return maxDisplacement;
	}

	public void setMaxDisplacement(double maxDisplacement) {
		this.maxDisplacement = maxDisplacement;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
}
