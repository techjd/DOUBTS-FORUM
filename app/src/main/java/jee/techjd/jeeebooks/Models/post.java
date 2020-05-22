package jee.techjd.jeeebooks.Models;

public class post {
    String mQuesId;
    String mSubject;
    String mquestionTilte;
    String mDescription;
    String mimageUrl;
    String publisher;
    public post() {
    }

    public post(String subject, String questionTitle, String Description, String ImageURL, String questionID,String Publisher) {
        this.mSubject = subject;
        this.mquestionTilte = questionTitle;
        this.mDescription = Description;
        this.mimageUrl = ImageURL;
        this.mQuesId = questionID;
        this.publisher = Publisher;

    }

    public String getmSubject() {
        return mSubject;
    }

    public void setmSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public String getMquestionTilte() {
        return mquestionTilte;
    }

    public void setMquestionTilte(String mquestionTilte) {
        this.mquestionTilte = mquestionTilte;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getMimageUrl() {
        return mimageUrl;
    }

    public void setMimageUrl(String mimageUrl) {
        this.mimageUrl = mimageUrl;
    }

    public String getmQuesId() {
        return mQuesId;
    }

    public void setmQuesId(String mQuesId) {
        this.mQuesId = mQuesId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
