import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Parser {

    private ArrayList<String> stationsList = new ArrayList<>();
    private ArrayList<String> connectionsListTemp = new ArrayList<>();
    private ArrayList<String> connectionsList = new ArrayList<>();
    private String lineTemp = null;
    private String stationTemp = null;
    private TreeMap<String, String> linesMap = new TreeMap();
    private TreeMap<String, String> colorMap = new TreeMap();
    ArrayList<Line> metroLines = new ArrayList<>();
    private List<String> stationsNames = new ArrayList<>();
    TreeMap<String, List<String>> stations = new TreeMap<>();
    private List<HashSet<Station>> connectionsTemp = new ArrayList<>();
    private HashSet<Station> bufer = new HashSet<>();
    HashSet<HashSet<Station>> connections;


    public Parser(String path) {
        try {
            Document doc = Jsoup.connect(path).maxBodySize(0).get();
            Elements lines = doc.select("td");

            for (Element line : lines) {

                if (line.getElementsByTag("span").attr("title").matches("[А-яёЁ\\-\\s]+\\s+линия") ||
                        line.getElementsByTag("span").attr("title").matches("[А-яёЁ\\-\\s]+\\s+кольцо") ||
                        line.getElementsByTag("span").attr("title").matches("[А-яёЁ\\-\\s]+\\s+монорельс")) {
                    if (line.getElementsByTag("span").get(0).text().charAt(0) == '0') {
                        linesMap.put(line.getElementsByTag("span").get(0).text().substring(1), line.getElementsByTag("span").attr("title"));
                        lineTemp = line.getElementsByTag("span").get(0).text().substring(1);
                        if (line.attr("style").contains("background") || line.attr("style").contains("display")) {
                            colorMap.put(line.getElementsByTag("span").get(0).text().substring(1), line.attr("style").substring(11));
                        }
                    } else {
                        linesMap.put(line.getElementsByTag("span").get(0).text(), line.getElementsByTag("span").attr("title"));
                        lineTemp = line.getElementsByTag("span").get(0).text();
                        if (line.attr("style").contains("background") || line.attr("style").contains("display")) {
                            colorMap.put(line.getElementsByTag("span").get(0).text(), line.attr("style").substring(11));
                        }
                    }
                }

                if (line.getElementsByTag("a").attr("title").contains("станция метро") ||
                        line.getElementsByTag("a").attr("title").contains("станция монорельса") ||
                        line.getElementsByTag("a").attr("title").contains("станция МЦК") ||
                        line.getElementsByTag("a").attr("title").contains("Шаболовская") ||
                        line.getElementsByTag("a").attr("title").contains("Полежаевская") ||
                        line.getElementsByTag("a").attr("title").contains("Фонвизинская")
                ) {
                    if (line.getElementsByTag("a").attr("title").contains("Переход")) {
                        continue;
                    }
                    if (line.getElementsByTag("a").attr("title").contains("закрытая станция метро")) {
                        break;
                    }
                    if (line.getElementsByTag("a").attr("title").contains("(станция")) {
                        stationsList.add(line.getElementsByTag("a").attr("title").substring(0, line.getElementsByTag("a").
                                attr("title").indexOf("(") - 1) + " = " + lineTemp);
                        stationTemp = line.getElementsByTag("a").attr("title").substring(0, line.getElementsByTag("a").
                                attr("title").indexOf("(") - 1) + " = " + lineTemp;
                    } else {
                        stationsList.add(line.getElementsByTag("a").attr("title") + " = " + lineTemp);
                        stationTemp = line.getElementsByTag("a").attr("title") + " = " + lineTemp;
                    }
                }

                if (line.getElementsByTag("a").attr("title").contains("Переход") ||
                        line.getElementsByTag("a").attr("title").contains("Кросс-платформенная")) {
                    for (Element element : line.getElementsByTag("span")) {
                        if (!element.getElementsByTag("span").text().isEmpty()) {
                            if (element.text().charAt(0) == '0') {
                                if (line.getElementsByTag("a").attr("title").contains("Кросс-платформенная")) {
                                    connectionsListTemp.add(stationTemp + " > " + element.text().substring(1) + " = " +
                                            element.nextElementSibling().attr("title").
                                                    substring(41));
                                }
                                connectionsListTemp.add(stationTemp + " > " + element.text().substring(1) + " = " +
                                        element.nextElementSibling().attr("title").
                                                substring(19));
                            } else {
                                if (line.getElementsByTag("a").attr("title").contains("Кросс-платформенная")) {
                                    connectionsListTemp.add(stationTemp + " > " + element.text() + " = " + element.nextElementSibling().attr("title").
                                            substring(41));
                                }
                                connectionsListTemp.add(stationTemp + " > " + element.text() + " = " + element.nextElementSibling().attr("title").
                                        substring(19));
                            }
                        }
                    }
                }
            }

            for (String string : connectionsListTemp) {
                String key = string.substring(string.indexOf(">") + 1, string.lastIndexOf("=") - 1).trim();
                int lenght = linesMap.get(key).length();
                if (key.equals("14") || key.equals("13")) {
                    string = string.substring(0, string.length() - lenght - 2);
                } else {
                    string = string.substring(0, string.length() - lenght);
                }
                connectionsList.add(string);
            }

            for (String key : linesMap.keySet()) {
                if (key.contains("А")) {
                    metroLines.add(new Line(key, linesMap.get(key), colorMap.get(key.replaceAll("А", ""))));
                } else {
                    metroLines.add(new Line(key, linesMap.get(key), colorMap.get(key)));
                }
            }

            for (Line line : metroLines) {
                for (String station : stationsList) {
                    if (station.substring(station.indexOf("=") + 1).trim().equals(line.getNumber().trim()))
                        line.addStation(new Station(station.substring(0, station.indexOf("=") - 1).trim(), line));
                }
            }


            for (Line line : metroLines) {
                stationsNames.clear();
                for (Station station : line.getStations()) {
                    stationsNames.add(station.getName());
                }
                stations.put(line.getNumber(), new ArrayList<>(stationsNames));
            }

            for (String string : connectionsList) {
                bufer.clear();
                for (Line line : metroLines) {
                    if (string.substring(string.indexOf("=") + 1, string.lastIndexOf(">") - 1).trim().equals(line.getNumber())) {
                        for (Station station : line.getStations()) {
                            if (string.substring(0, string.indexOf("=") - 1).trim().equals(station.getName())) {
                                bufer.add(station);
                                for (Line connectedLine : metroLines) {
                                    if (string.substring(string.indexOf(">") + 1, string.lastIndexOf("=") - 1).trim().equals(connectedLine.getNumber())) {
                                        for (Station connectedStation : connectedLine.getStations()) {
                                            if (string.substring(string.lastIndexOf("=") + 1).trim().equals(connectedStation.getName())) {
                                                bufer.add(connectedStation);
                                                connectionsTemp.add(new HashSet<>(bufer));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ArrayList<HashSet<Station>> connectionsCopy = new ArrayList<>(connectionsTemp);

            for (HashSet<Station> oldTreeSet : connectionsTemp) {
                for (Station oldStation : oldTreeSet) {
                    for (HashSet<Station> newTreeSet : connectionsCopy) {
                        if (newTreeSet.contains(oldStation)) {
                            newTreeSet.addAll(oldTreeSet);
                        }
                    }
                }
            }
            connections = new HashSet<>(connectionsCopy);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


