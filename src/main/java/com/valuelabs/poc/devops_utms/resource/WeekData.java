package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;

public class WeekData implements Serializable {
	
	private String label;
    private int analysis;
    private int commits;
    private int build;
    private int deploy;

   public WeekData(){

   }

public String getLabel() {
	return label;
}

public void setLabel(String label) {
	this.label = label;
}

public int getAnalysis() {
	return analysis;
}

public void setAnalysis(int analysis) {
	this.analysis = analysis;
}

public int getCommits() {
	return commits;
}

public void setCommits(int commits) {
	this.commits = commits;
}

public int getBuild() {
	return build;
}

public void setBuild(int build) {
	this.build = build;
}

public int getDeploy() {
	return deploy;
}

public void setDeploy(int deploy) {
	this.deploy = deploy;
}

@Override
public String toString() {
	return "WeekData [label=" + label + ", analysis=" + analysis + ", commits=" + commits + ", build=" + build
			+ ", deploy=" + deploy + "]";
}
 }
