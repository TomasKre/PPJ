package io.swagger.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import io.swagger.configuration.LocalDateConverter;
import io.swagger.configuration.LocalDateTimeConverter;
import io.swagger.model.Cities;
import io.swagger.model.City;
import io.swagger.model.Wind;
import io.swagger.model.openweather.OpenWeather;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.swagger.Swagger2SpringBoot;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Service
@Configurable
public class FetchDataService {
    @Autowired
    private Environment env;
    private static final Logger log = LoggerFactory.getLogger(FetchDataService.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoURI;
    private MongoDatabase database; //Mongo
    @Value("${spring.datasource.url}")
    private String sqlURI;
    private Connection connection; //SQL
    private Cities cities;
    @Value("${open.weather.api.key}")
    private String apiKey;


    public FetchDataService() {
        Runnable fetchData = () -> fetchCities();
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(fetchData , 10, 86400, TimeUnit.SECONDS);
    }

    private void fetchCities() {
        if (this.connection == null) {
            initializeSQLConnection();
        }
        if (this.cities == null) {
            this.cities = new Cities();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id, name, country," +
                            " lon, lat FROM City");

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                City city = new City();
                city.setId(rs.getInt(1));
                city.setName(rs.getString(2));
                city.setCountry(rs.getString(3));
                city.setLon(rs.getFloat(4));
                city.setLat(rs.getFloat(5));
                cities.add(city);
            }
            fetchData();
        } catch (SQLException e) {
            log.error("Error getting cities list", e);
        }
        log.info("Fetched new data for cities and weather");
    }

    private void fetchData() {
        if (this.database == null) {
            initializeMongoConnection();
        }
        try {
            MongoCollection<Document> collection = database.getCollection("weather");
            for (City city : this.cities) {
                List<Document> documents = new ArrayList<>();
                String apiURL ="https://api.openweathermap.org/data/2.5/forecast?lat=" + city.getLat() +
                        "&lon=" + city.getLon() + "&appid=" + apiKey;
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                con.disconnect();

                ObjectMapper om = new ObjectMapper();
                OpenWeather root = om.readValue(content.toString(), OpenWeather.class);

                Document doc = new Document("city", new Document("country", city.getCountry())
                        .append("id", city.getId()).append("lat", city.getLat())
                        .append("lon", city.getLon()).append("name", city.getName()));
                for (int i = 0; i < root.list.size(); i++) {
                    io.swagger.model.openweather.List rootListI = root.list.get(i);
                    Document wind = new Document("wind", new Document("speed", rootListI.wind.deg)
                            .append("deg", rootListI.wind.deg).append("gust", rootListI.wind.gust));
                    Document listI = new Document("dt", rootListI.dt).append("dt_txt", rootListI.dt_txt)
                            .append("temp", rootListI.main.temp).append("feels_like", rootListI.main.feels_like)
                            .append("temp_min", rootListI.main.temp_min).append("temp_max", rootListI.main.temp_max)
                            .append("pressure", rootListI.main.pressure).append("sea_level", rootListI.main.sea_level)
                            .append("grnd_level", rootListI.main.grnd_level).append("humidity", rootListI.main.humidity)
                            .append("wind", wind).append("visibility", rootListI.visibility);
                    documents.add(listI);
                }
                doc.append("forecast", Arrays.asList(documents));
                try {
                    collection.insertOne(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("Inserted " + documents.size() + " documents of city " + city.getName());
            }
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeMongoConnection() {
        try {
            MongoClient client = MongoClients.create(mongoURI);
            this.database = client.getDatabase("PPJ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeSQLConnection() {
        try {
            this.connection = DriverManager.getConnection(sqlURI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
