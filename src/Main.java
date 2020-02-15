import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String path = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
        Parser parser = new Parser(path);
//        ArrayList<Line> lines = new ArrayList<>();
//        TreeMap<String, List<String>> stations = new TreeMap<>();
        Writer writer = new FileWriter("data/map.json");
//        for (String key : parser.linesMap.keySet()) {
//            if (key.contains("А")){
//                lines.add(new Line(key, parser.linesMap.get(key), parser.colorMap.get(key.replaceAll("А", "") )));
//            }
//            else {
//                lines.add(new Line(key, parser.linesMap.get(key), parser.colorMap.get(key)));
//            }
//        }
//        for (Line line : lines) {
//            for (String station : parser.stationsList) {
//                if (station.substring(station.indexOf("=") + 1).trim().equals(line.getNumber().trim()))
//                    line.addStation(new Station(station.substring(0, station.indexOf("=") - 1).trim(), line));
//            }
//        }
////
//        for (Line line : lines) {
//            stations.put(line.getNumber(), line.getStationsNames());
//        }


        ToJson toJson = new ToJson(parser.metroLines, parser.stations);
        gson.toJson(toJson, writer);
        System.out.println("ji est'");



//        printMap(parser.linesMap);
//
//        System.out.println("=========================");
//
//        printMap(parser.colorMap);

//        parser.stationsList.forEach(System.out::println);
//
//        System.out.println("=========================");
//        parser.connectionsList.forEach(System.out::println);


    }

    private static void printMap(Map<String, String> map) {
        for (String key : map.keySet()) {
            System.out.println(key + "\t=>\t" + map.get(key));
        }
    }
}
