package jee.techjd.jeeebooks.Models;

public class solution {
    String publisher;
    String solution;
    String solutionId;
    String mimageUrl;

    public solution() {
    }

    public solution(String publisher, String solution, String solutionId, String mimageUrl) {
        this.publisher = publisher;
        this.solution = solution;
        this.solutionId = solutionId;
        this.mimageUrl = mimageUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public String getMimageUrl() {
        return mimageUrl;
    }

    public void setMimageUrl(String mimageUrl) {
        this.mimageUrl = mimageUrl;
    }
}
