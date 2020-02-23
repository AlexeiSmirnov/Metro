import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String path = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
        Parser parser = new Parser(path);
        Writer writer = new FileWriter("data/map.json");

        ToJson toJson = new ToJson(parser.metroLines, parser.stations, parser.connections);
        gson.toJson(toJson, writer);
        writer.close();

        FileReader reader = new FileReader("data/map.json");


        ToJson fromJson = gson.fromJson(reader, ToJson.class);
        fromJson.stations.keySet().stream().forEach(k -> System.out.println("На линии " + k + " => " + fromJson.stations.get(k).size() + " станций"));
        reader.close();

    }
}
