package io.swagger.api;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import io.swagger.annotations.*;
import io.swagger.model.*;
import io.swagger.model.openweather.OpenWeather;
import org.bson.Document;

import java.time.Instant;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")

@Controller
public class WeatherApiController implements WeatherApi {

    private static final Logger log = Logger.getLogger(WeatherApiController.class.getName());
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    @Autowired
    private Environment env;
    private static MongoDatabase database;
    private static Connection connection;

    @org.springframework.beans.factory.annotation.Autowired
    public WeatherApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;;

        FileHandler fh;
        try {
            fh = new FileHandler("./Weather.log", true);
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            log.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    public void initializeMongoConnection() {
        try {
            String connectionString = env.getProperty("spring.data.mongodb.uri");
            MongoClient client = MongoClients.create(connectionString);
            this.database = client.getDatabase("PPJ");

        } catch (Exception e) {
            e.printStackTrace();
            log.severe(e.getMessage());
        }
    }

    public void initializeSQLConnection() {
        try {
            this.connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"));
        } catch (SQLException e) {
            e.printStackTrace();
            log.severe(e.getMessage());
        }
    }

    public ResponseEntity<String> getWeather(@ApiParam(value = "",required=true) @PathVariable("city_name") String city_name) {
        if (this.database == null) {
            initializeMongoConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                MongoCollection<Document> collection = database.getCollection("weather");

                FindIterable<Document> iterator = collection.find(eq("city.name", city_name));

                String response = "";
                try {
                    for (Document document : iterator) {
                        response = iterator.cursor().next().toJson();
                    }
                } finally {
                    iterator.cursor().close();
                }
                log.info("Executed getWeather method");
                return new ResponseEntity<String>(response, HttpStatus.OK);
            } catch (Exception e) {
                log.severe("Error executing getWeather " + e.getMessage());
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Error at end of getWeather method");
        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<String> exportWeather() {
        if (this.database == null) {
            initializeMongoConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                MongoCollection<Document> collection = database.getCollection("weather");

                FindIterable<Document> iterator = collection.find();

                StringBuilder response = new StringBuilder();
                try {
                    int i = 0;
                    for (Document document : iterator.projection(excludeId())) {
                        String json = document.toJson();
                        Weather weather = new ObjectMapper().readValue(json, Weather.class);
                        response.append(weather.getCity().getName() + ",");
                        response.append(weather.getCity().getCountry() + ",");
                        response.append(weather.getCity().getLat() + ",");
                        response.append(weather.getCity().getLon() + ",");

                        response.append(weather.getForecast().getDt() + ",");
                        response.append(weather.getForecast().getDtTxt() + ",");
                        response.append(weather.getForecast().getTemp() + ",");
                        response.append(weather.getForecast().getFeelsLike() + ",");
                        response.append(weather.getForecast().getTempMin() + ",");
                        response.append(weather.getForecast().getTempMax() + ",");
                        response.append(weather.getForecast().getPressure() + ",");
                        response.append(weather.getForecast().getSeaLevel() + ",");
                        response.append(weather.getForecast().getGrndLevel() + ",");
                        response.append(weather.getForecast().getHumidity() + ",");

                        response.append(weather.getForecast().getWind().getSpeed() + ",");
                        response.append(weather.getForecast().getWind().getDeg() + ",");
                        response.append(weather.getForecast().getWind().getGust() + ",");

                        response.append(weather.getForecast().getVisibility() + "\n");
                        i++;
                    }
                } finally {
                    iterator.cursor().close();
                }
                log.info("Executed exportWeather method");
                return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
            } catch (Exception e) {
                log.severe("Error executing exportWeather " + e.getMessage());
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Error at end of exportWeather method");
        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<String> importWeather(@ApiParam(value = "" ,required=true )  @Valid @RequestBody String csv) {
        if (this.database == null) {
            initializeMongoConnection();
        }
        if (this.connection == null) {
            initializeSQLConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            String[] lines = csv.split("\n");
            MongoCollection<Document> collection = database.getCollection("weather");
            List<Document> documents = new ArrayList<>();

            int i = 0;
            for (String line : lines) {
                try {
                String[] parts = line.split(",");
                Document doc = new Document("city", new Document("name", parts[0])
                    .append("country", parts[1]).append("lat", parts[2]).append("lon", parts[3]));
                Document wind = new Document("speed", parts[14])
                        .append("deg", parts[15]).append("gust", parts[16]);
                Document forecast = new Document("dt", parts[4]).append("dt_txt", parts[5])
                        .append("temp", parts[6]).append("feels_like", parts[7])
                        .append("temp_min", parts[8]).append("temp_max", parts[9])
                        .append("pressure", parts[10]).append("sea_level", parts[11])
                        .append("grnd_level", parts[12]).append("humidity", parts[13])
                        .append("wind", wind).append("visibility", parts[17]);
                doc.append("forecast", forecast);
                documents.add(doc);
                i++;
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
            try {
                collection.insertMany(documents);
                log.info("Imported " + i + " measurements");
                return new ResponseEntity<String>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.severe("Error inserting documents to Mongo Atlas " + e.getMessage());
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of importWeather method");
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Weather> getAvarageWeather(@ApiParam(value = "",required=true) @PathVariable("city_name") String city_name, @ApiParam(value = "",required=true) @PathVariable("days_back") Integer days_back) {
        if (this.database == null) {
            initializeMongoConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                MongoCollection<Document> collection = database.getCollection("weather");

                long unixTime = Instant.now().getEpochSecond();

                FindIterable<Document> iterator = collection.find(and(eq("city.name", city_name),
                        and(lt("forecast.dt", unixTime), gt("forecast.dt", unixTime - days_back * 86400))));

                Weather weatherOut = new Weather();
                try {
                    int count = 0;
                    double temp = 0;
                    double feels_like = 0;
                    double temp_max = 0;
                    double temp_min = 0;
                    double pressure = 0;
                    double sea_level = 0;
                    double grnd_level = 0;
                    double humidity = 0;
                    double speed = 0;
                    double gust = 0;
                    double visibility = 0;
                    boolean city_set = false;
                    City city = new City();
                    Forecast forecast = new Forecast();
                    for (Document document : iterator.projection(excludeId())) {
                        String json = document.toJson();
                        Weather weather = new ObjectMapper().readValue(json, Weather.class);
                        if (!(city_set)) {
                            city.setId(weather.getCity().getId());
                            city.setName(weather.getCity().getName());
                            city.setCountry(weather.getCity().getCountry());
                            city.setLat(weather.getCity().getLat());
                            city.setLon(weather.getCity().getLon());
                            city_set = true;
                        }
                        temp += weather.getForecast().getTemp();
                        feels_like += weather.getForecast().getFeelsLike();
                        temp_max += weather.getForecast().getTempMax();
                        temp_min += weather.getForecast().getTempMin();
                        pressure += weather.getForecast().getPressure();
                        sea_level += weather.getForecast().getSeaLevel();
                        grnd_level += weather.getForecast().getGrndLevel();
                        humidity += weather.getForecast().getHumidity();
                        speed += + weather.getForecast().getWind().getSpeed();
                        gust += weather.getForecast().getWind().getGust();
                        visibility += weather.getForecast().getVisibility();
                        count++;
                    }
                    forecast.setTemp((float)temp / count);
                    forecast.setFeelsLike((float)feels_like / count);
                    forecast.setTempMax((float)temp_max / count);
                    forecast.setTempMin((float)temp_min / count);
                    forecast.setPressure((int)pressure / count);
                    forecast.setSeaLevel((int)sea_level / count);
                    forecast.setGrndLevel((int)grnd_level / count);
                    forecast.setHumidity((int)humidity / count);
                    Wind wind = new Wind();
                    wind.setSpeed((float)speed / count);
                    wind.setDeg(null);
                    wind.gust((float)gust / count);
                    forecast.setWind(wind);
                    forecast.setVisibility((int)visibility / count);
                    weatherOut.setCity(city);
                    weatherOut.setForecast(forecast);
                } finally {
                    iterator.cursor().close();
                }
                log.info("Executed getAverageWeather method");
                return new ResponseEntity<Weather>(weatherOut, HttpStatus.OK);
            } catch (Exception e) {
                log.severe("Error executing getWeather " + e.getMessage());
                return new ResponseEntity<Weather>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Error at end of getWeather method");
        return new ResponseEntity<Weather>(HttpStatus.NOT_IMPLEMENTED);
    }

}
