package core;

import com.google.gson.annotations.Expose;

public class Station implements Comparable<Station>
{
    private String line;
    private transient Line line1;
    private String name;


    public Station(String name, Line line1)
    {
        this.name = name;
        this.line1 = line1;
        line = line1.getNumber();
    }

    public Line getLine()
    {
        return line1;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public int compareTo(Station station)
    {
        int lineComparison = line1.compareTo(station.getLine());
        if(lineComparison != 0) {
            return lineComparison;
        }
        return name.compareToIgnoreCase(station.getName());
    }

    @Override
    public boolean equals(Object obj)
    {
        return compareTo((Station) obj) == 0;
    }

    @Override
    public String toString()
    {
        return name;
    }
}