import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Parser {

    ArrayList<String> stationsList = new ArrayList<>();
    private ArrayList<String> connectionsListTemp = new ArrayList<>();
    ArrayList<String> connectionsList = new ArrayList<>();
    private String lineTemp = null;
    private String stationTemp = null;
    TreeMap<String, String> linesMap = new TreeMap();
    TreeMap<String, String> colorMap = new TreeMap();
    ArrayList<Line> metroLines = new ArrayList<>();
    private List<String> stationsNames = new ArrayList<>();
    TreeMap<String, List<String>> stations = new TreeMap<>();

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

                if (line.getElementsByTag("a").attr("title").contains("Переход")) {
                    for (Element element : line.getElementsByTag("span")) {
                        if (!element.getElementsByTag("span").text().isEmpty()) {
                            if (element.text().charAt(0) == '0') {
                                connectionsListTemp.add(stationTemp + " -> " + element.text().substring(1) + " = " +
                                        element.nextElementSibling().attr("title").
                                                substring(19));
                            } else {
                                connectionsListTemp.add(stationTemp + " -> " + element.text() + " = " + element.nextElementSibling().attr("title").
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
                stations.put(line.getNumber(), stationsNames);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


