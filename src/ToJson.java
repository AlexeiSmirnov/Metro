import core.Line;
import core.Station;

import java.util.*;

public class ToJson {

    TreeMap<String, List<String>> stations;
    HashSet<HashSet<Station>> connections;
    ArrayList<Line> lines;


    public ToJson(ArrayList<Line> lines, TreeMap<String, List<String>> stations, HashSet<HashSet<Station>> connections) {

        this.lines = lines;
        this.stations = stations;
        this.connections = connections;
    }

    public TreeMap<String, List<String>> getStations() {
        return stations;
    }

    public void setStations(TreeMap<String, List<String>> stations) {
        this.stations = stations;
    }


    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

}
