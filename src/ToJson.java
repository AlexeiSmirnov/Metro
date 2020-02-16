import com.google.gson.annotations.Expose;
import core.Line;
import core.Station;

import java.util.*;

public class ToJson {
    TreeMap<String, List<String>> stations;

    ArrayList<Line> lines;

    //    ArrayList<String> connectionsList;
    List<TreeSet<Station>> connections;


    public ToJson(ArrayList<Line> lines, TreeMap<String, List<String>> stations, List<TreeSet<Station>> connections) {

        this.stations = stations;
        this.lines = lines;
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
