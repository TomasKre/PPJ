package io.swagger.api;

import io.swagger.model.Cities;
import io.swagger.model.City;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")

// Prepared statement chrání před SQL Injection

@Controller
public class CityApiController implements CityApi {

    private static final Logger log = Logger.getLogger(CityApiController.class.getName());

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private Environment env;

    private static Connection connection;

    @Autowired
    public CityApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;

        FileHandler fh;
        try {
            fh = new FileHandler("./City.log", true);
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            log.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    public void initializeConnection() {
        try {
            this.connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"));
        } catch (SQLException e) {
            e.printStackTrace();
            log.severe(e.getMessage());
        }
    }

    public ResponseEntity<City> deleteCity(@ApiParam(value = "",required=true) @PathVariable("city_name") String city_name) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM City WHERE city = ?");
                preparedStatement.setString(1, city_name);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    log.info("Deleted city: " + city_name);
                    return new ResponseEntity<City>(HttpStatus.NO_CONTENT);
                }
                log.severe("Error deleting city: " + city_name);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (SQLException e) {
                log.severe("Error deleting city: " + city_name + " " + e.getMessage());
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of deleteCity method");
        return new ResponseEntity<City>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Cities> getCities() {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id, name, country," +
                                " lon, lat FROM City");

                ResultSet rs = preparedStatement.executeQuery();
                Cities cities = new Cities();
                while(rs.next()) {
                    City city = new City();
                    city.setId(rs.getInt(1));
                    city.setName(rs.getString(2));
                    city.setCountry(rs.getString(3));
                    city.setLon(rs.getFloat(4));
                    city.setLat(rs.getFloat(5));
                    cities.add(city);
                }

                log.info("Executed getCities");
                return new ResponseEntity<Cities>(cities, HttpStatus.OK);
            } catch (SQLException e) {
                log.severe("Error getting Cities " + e.getMessage());
                return new ResponseEntity<Cities>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of getCities method");
        return new ResponseEntity<Cities>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<City> getCity(@ApiParam(value = "",required=true) @PathVariable("city_name") String city_name) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id, name, country," +
                        " lon, lat FROM City WHERE city = ?");
                preparedStatement.setString(1, city_name);

                ResultSet rs = preparedStatement.executeQuery();
                rs.next();

                City city = new City();
                city.setId(rs.getInt(1));
                city.setName(rs.getString(2));
                city.setCountry(rs.getString(3));
                city.setLon(rs.getFloat(4));
                city.setLat(rs.getFloat(5));

                log.info("Executed getCity: " + city_name);
                return new ResponseEntity<City>(city, HttpStatus.OK);
            } catch (SQLException e) {
                log.severe("Error getting City: " + city_name + " " + e);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of getCity method");
        return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<City> postCity(@ApiParam(value = "" ,required=true )  @Valid @RequestBody City body) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                ResultSet rs;
                PreparedStatement preparedStatement;

                preparedStatement = connection.prepareStatement("SELECT country FROM Country" +
                        " WHERE country = ?");
                preparedStatement.setString(1, body.getCountry());
                rs = preparedStatement.executeQuery();
                rs.next();
                String test = rs.getString(1); // If country doesn't exist SQLException

                try {
                    preparedStatement = connection.prepareStatement("INSERT INTO City VALUES (" +
                            "?, ?, ?, ?)");
                    preparedStatement.setString(1, body.getName());
                    preparedStatement.setString(2, body.getCountry());
                    preparedStatement.setFloat(3, body.getLon());
                    preparedStatement.setFloat(4, body.getLat());

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        log.info("Created new City");
                        return new ResponseEntity<City>(HttpStatus.CREATED);
                    }
                    log.severe("Error creating new City");
                    return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (SQLException e) {
                    log.severe("Error creating new City " + e.getMessage());
                    return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (SQLException e) {
                log.severe("Country code doesn't exist " + e.getMessage());
                return new ResponseEntity<City>(HttpStatus.BAD_REQUEST);
            }
        }
        log.severe("Unknown error end of postCity method");
        return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<City> updateCity(@ApiParam(value = "",required=true) @PathVariable("city_name") String city_name,@ApiParam(value = "" ,required=true )  @Valid @RequestBody City body) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                ResultSet rs;
                PreparedStatement preparedStatement;

                preparedStatement = connection.prepareStatement("SELECT country FROM Country" +
                        " WHERE country = ?");
                preparedStatement.setString(1, body.getCountry());
                rs = preparedStatement.executeQuery();
                rs.next();
                String test = rs.getString(1); // If country doesn't exist SQLException

                try {
                    preparedStatement = connection.prepareStatement("UPDATE City SET " +
                            " name = ?, country = ?, lon = ?, lat = ? WHERE city_name = ?");
                    preparedStatement.setString(1, body.getName());
                    preparedStatement.setString(2, body.getCountry());
                    preparedStatement.setFloat(3, body.getLon());
                    preparedStatement.setFloat(4, body.getLat());
                    preparedStatement.setString(5, city_name);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        log.info("Updated City: " + city_name);
                        return new ResponseEntity<City>(HttpStatus.CREATED);
                    }
                    log.severe("Error updating City: " + city_name);
                    return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (SQLException e) {
                    log.severe("Error updating City: " + city_name + " " + e.getMessage());
                    return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (SQLException e) {
                log.severe("Country code doesn't exist " + e.getMessage());
                return new ResponseEntity<City>(HttpStatus.BAD_REQUEST);
            }
        }
        log.severe("Unknown error end of updateCity method");
        return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> exportCities() {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("text/plain") || accept.contains("text/csv"))) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT name, country," +
                                " lon, lat FROM City");

                ResultSet rs = preparedStatement.executeQuery();
                StringBuilder sb = new StringBuilder();
                while(rs.next()) {
                    sb.append(rs.getString(1) + ",");
                    sb.append(rs.getString(2) + ",");
                    sb.append(rs.getFloat(3) + ",");
                    sb.append(rs.getFloat(4) + "\n");
                }

                log.info("Executed exportCities");
                return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);
            } catch (SQLException e) {
                log.severe("Error exporting Cities " + e.getMessage());
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of exportCities method");
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> importCities(@ApiParam(value = "" ,required=true )  @Valid @RequestBody String csv) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            String[] lines = csv.split("\n");
            ResultSet rs;
            PreparedStatement preparedStatement;
            ArrayList<String> countries = new ArrayList<>();
            try {
                preparedStatement = connection.prepareStatement("SELECT country FROM Country");
                rs = preparedStatement.executeQuery();
                while(rs.next()) {
                    countries.add(rs.getString(1));
                }
            } catch (SQLException e) {
                log.severe("Error getting countries in importCities");
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            int correct = 0;
            int i = 1;
            String response = "";
            for (String line : lines) {
                String[] parts = line.split(",");
                if (countries.contains(parts[2])) {
                    try {
                        preparedStatement = connection.prepareStatement("INSERT INTO City VALUES (" +
                                "?, ?, ?, ?)");
                        preparedStatement.setString(1, parts[0]);
                        preparedStatement.setString(2, parts[1]);
                        preparedStatement.setFloat(3, Float.parseFloat(parts[2]));
                        preparedStatement.setFloat(4, Float.parseFloat(parts[3]));

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected == 0) {
                            response += "Error creating city on line " + i + "\n";
                        } else {
                            correct++;
                        }
                    } catch (SQLException e) {
                        response += "Error creating city on line " + i + "\n";
                    }
                } else {
                    response += "Country code doesn't exist on line " + i + "\n";
                }
                i++;
            }
            log.info("Imported " + correct + " cities");
            return new ResponseEntity<String>(response, HttpStatus.CREATED);
        }
        log.severe("Unknown error end of importCities method");
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
