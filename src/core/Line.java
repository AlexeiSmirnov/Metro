package core;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line>
{
    private String number;
    private String name;
    private transient List<Station> stations;
//    private  List<String> stationsNames;
    private String color;

    public Line(String number, String name, String color)
    {
        this.number = number;
        this.name = name;
        this.color = color;
        stations = new ArrayList<>();
//        stationsNames = new ArrayList<>();
    }

//    public List<String> getStationsNames() {
//        for (Station station : stations)
//        {
//            stationsNames.add(station.getName());
//        }
//        return stationsNames;
//    }

    public String getNumber()
    {
        return number;
    }

    public String getName()
    {
        return name;
    }

    public void addStation(Station station)
    {
        stations.add(station);
    }

    public List<Station> getStations()
    {
        return stations;
    }


    @Override
    public int compareTo(Line line)
    {
        return CharSequence.compare(number, line.getNumber());
    }

    @Override
    public boolean equals(Object obj)
    {
        return compareTo((Line) obj) == 0;
    }
}