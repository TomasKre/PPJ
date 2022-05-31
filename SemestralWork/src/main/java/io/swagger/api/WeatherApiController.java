package io.swagger.api;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.model.Cities;
import io.swagger.model.City;
import io.swagger.model.openweather.OpenWeather;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static com.mongodb.client.model.Filters.eq;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")

@Controller
public class WeatherApiController implements WeatherApi {

    private static final Logger log = LoggerFactory.getLogger(WeatherApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    @Autowired
    private Environment env;
    private static MongoDatabase database;
    private static Connection connection;

    @org.springframework.beans.factory.annotation.Autowired
    public WeatherApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public void initializeMongoConnection() {
        try {
            String connectionString = env.getProperty("spring.data.mongodb.uri");
            MongoClient client = MongoClients.create(connectionString);
            this.database = client.getDatabase("PPJ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeSQLConnection() {
        try {
            this.connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"));
        } catch (SQLException e) {
            e.printStackTrace();
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
                log.error("Error executing getWeather", e);
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Error at end of getWeather method");
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

                String response = "";
                try {
                    for (Document document : iterator) {
                        response += iterator.cursor().next().toString() + "\n";
                    }
                } finally {
                    iterator.cursor().close();
                }
                log.info("Executed exportWeather method");
                return new ResponseEntity<String>(response, HttpStatus.OK);
            } catch (Exception e) {
                log.error("Error executing exportWeather", e);
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Error at end of exportWeather method");
        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<String> importCities(@ApiParam(value = "",required=true) @PathVariable("city_name") String city_name, @ApiParam(value = "" ,required=true )  @Valid @RequestBody String csv) {
        if (this.database == null) {
            initializeMongoConnection();
        }
        if (this.connection == null) {
            initializeSQLConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            City city = new City();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id, name, country, lon, lat FROM City WHERE name = ?");
                preparedStatement.setString(1, city_name);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    city.setId(rs.getInt(1));
                    city.setName(rs.getString(2));
                    city.setCountry(rs.getString(3));
                    city.setLon(rs.getFloat(4));
                    city.setLat(rs.getFloat(5));
                }
            } catch (SQLException e) {
                log.error("Error getting city info");
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String[] lines = csv.split("\n");
            MongoCollection<Document> collection = database.getCollection("weather");
            List<Document> documents = new ArrayList<>();

            Document doc = new Document("city", new Document("country", city.getCountry())
                    .append("id", city.getId()).append("lat", city.getLat())
                    .append("lon", city.getLon()).append("name", city.getName()));
            for (String line : lines) {
                try {
                String[] parts = line.split(",");
                Document wind = new Document("wind", new Document("speed", parts[10])
                        .append("deg", parts[11]).append("gust", parts[12]));
                Document listI = new Document("dt", parts[0]).append("dt_txt", parts[1])
                        .append("temp", parts[2]).append("feels_like", parts[3])
                        .append("temp_min", parts[4]).append("temp_max", parts[5])
                        .append("pressure", parts[6]).append("sea_level", parts[7])
                        .append("grnd_level", parts[8]).append("humidity", parts[9])
                        .append("wind", wind).append("visibility", parts[13]);
                documents.add(listI);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
            doc.append("forecast", Arrays.asList(documents));
            try {
                collection.insertOne(doc);
                log.info("Inserted measurements document of city " + city.getName());
                return new ResponseEntity<String>(HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.error("Unknown error end of importWeather method");
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
