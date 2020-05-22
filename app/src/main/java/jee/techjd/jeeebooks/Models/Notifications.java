package jee.techjd.jeeebooks.Models;

public class Notifications {
    String publisher;
    boolean isSolution;
    String solutionId;
    String text;

    public Notifications(String publisher, boolean isSolution, String solutionId, String text) {
        this.publisher = publisher;
        this.isSolution = isSolution;
        this.solutionId = solutionId;
        this.text = text;
    }

    public Notifications() {

    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isSolution() {
        return isSolution;
    }

    public void setSolution(boolean solution) {
        isSolution = solution;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
