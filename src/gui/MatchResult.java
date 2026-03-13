package gui;

public class MatchResult {

    private boolean matched;
    private String name;
    private String crime;
    private String imagePath;

    public MatchResult(boolean matched, String name, String crime, String imagePath) {
        this.matched = matched;
        this.name = name;
        this.crime = crime;
        this.imagePath = imagePath;
    }

    public boolean isMatched() {
        return matched;
    }

    public String getName() {
        return name;
    }

    public String getCrime() {
        return crime;
    }

    public String getImagePath() {
        return imagePath;
    }
}