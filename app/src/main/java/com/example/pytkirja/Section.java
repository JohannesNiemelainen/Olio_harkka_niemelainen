package com.example.pytkirja;

public class Section {
    private int runningNumber;
    protected String introduction;
    protected String proposal;
    protected String decision;

    public Section () {
        runningNumber = 0;
        introduction = "";
        proposal = "";
        decision = "";
    }

    public int getRunningNumber() {
        return runningNumber;
    }

    public void setRunningNumber(int runningNumber) {
        this.runningNumber = runningNumber;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String toString() {
        return runningNumber + ". " + introduction;
    }
}
