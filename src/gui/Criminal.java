package gui;

public class Criminal {

    private int id;
    private String name;
    private String crime;

    public Criminal(int id, String name, String crime) {

        this.id = id;
        this.name = name;
        this.crime = crime;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCrime() {
        return crime;
    }
}