package org.example.utils;

public class PairUtils {
    String text;
    String annotations;

    public PairUtils(String text, String annotations) {
        this.text = text;
        this.annotations = annotations;
    }

    public String getText() {
        return text;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }
}
