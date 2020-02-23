package core;
import com.google.gson.annotations.SerializedName;

public class Station implements Comparable<Station> {

    private transient Line line;
    private String name;
    @SerializedName("line")
    private String lineNumber;

    public Station(String name, Line line) {

        this.name = name;

        this.line = line;
        lineNumber = line.getNumber();
    }

    public Line getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Station station) {
        int lineComparison = line.compareTo(station.getLine());
        if (lineComparison != 0) {
            return lineComparison;
        }
        return name.compareToIgnoreCase(station.getName());
    }


    public void setLine1(Line line1) {
        this.line = line1;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((Station) obj) == 0;
    }

    @Override
    public String toString() {
        return name;
    }
}