package io.swagger.api;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

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

    public ResponseEntity<String> getWeather(@ApiParam(value = "",required=true) @PathVariable("city_name") String city_name) {
        if (this.database == null) {
            initializeConnection();
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

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

}
