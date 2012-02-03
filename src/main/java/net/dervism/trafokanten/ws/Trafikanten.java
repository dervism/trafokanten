package net.dervism.trafokanten.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Bruk "http://api-test.trafikanten.no/Place/FindPlaces/montebello" for �
 * s�ke etter en destinasjon.
 * 
 * @author Dervis M
 * 
 */

public class Trafikanten extends Observable implements Runnable {

    private final List<Departure> departures = Collections.synchronizedList(new LinkedList<Departure>());

    @Override
    public void run() {
        while (true) {
            try {
                URL webservice = new URL("http://api-test.trafikanten.no/RealTime/GetRealTimeData/3012575");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(webservice.openStream()));
                try {
                    StringBuilder sb = new StringBuilder();
                    String inputLine;
                    
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    
                    Object obj = JSONValue.parse(sb.toString());
                    JSONArray array = (JSONArray) obj;
                    
                    for (Object o : array) {
                        JSONObject jsonObject = (JSONObject) o;
                        if (jsonObject.containsKey("DestinationName") &&
                                jsonObject.get("DeparturePlatformName").equals("1")) {
                            String destinationName = jsonObject.get("DestinationName").toString();
                            Long time = Long.valueOf(jsonObject.get("ExpectedDepartureTime").toString().substring(6).substring(0, 13));
                            departures.add(new Departure(destinationName, time));
                        }
                    }
                } finally {
                    in.close();
                }
                notifyListeners();
                departures.clear();
                Thread.sleep(10000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void notifyListeners() {
        if (countObservers() > 0) {
            setChanged();
            notifyObservers(departures);
        }
    }

    public List<Departure> getDepartures() {
        return departures;
    }
}
