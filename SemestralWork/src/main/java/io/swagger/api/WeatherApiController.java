package io.swagger.api;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.swagger.model.Weather;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")

@Controller
public class WeatherApiController implements WeatherApi {

    private static final Logger log = LoggerFactory.getLogger(WeatherApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    @Autowired
    private Environment env;
    private static MongoDatabase database;

    @org.springframework.beans.factory.annotation.Autowired
    public WeatherApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public void initializeConnection() {
        try {
            String connectionString = env.getProperty("spring.data.mongodb.uri");
            MongoClient client = MongoClients.create(connectionString);
            this.database = client.getDatabase("PPJ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<Weather> getWeather(@ApiParam(value = "",required=true) @PathVariable("city_name") String city_name) {
        if (this.database == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                MongoCollection<Document> collection = database.getCollection("weather");

                Long nDocs = collection.countDocuments();

                Weather weather = new Weather();

                Document myDoc = collection.find().first();
                System.out.println(myDoc.toJson());

                return new ResponseEntity<Weather>(HttpStatus.OK);
            } catch (Exception e) {
                log.error("Error", e);
                return new ResponseEntity<Weather>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Weather>(HttpStatus.NOT_IMPLEMENTED);
    }



}
