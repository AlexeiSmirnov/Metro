import com.google.gson.annotations.Expose;
import core.Line;
import core.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ToJson {


    TreeMap<String, List<String>> stations;
    ArrayList<Line> lines;

    public ToJson (ArrayList<Line> lines, TreeMap<String, List<String>> stations ){
        this.lines = lines;
        this.stations = stations;
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
