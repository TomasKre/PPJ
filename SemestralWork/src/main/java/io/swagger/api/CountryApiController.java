package io.swagger.api;

import io.swagger.model.City;
import io.swagger.model.Countries;
import io.swagger.model.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class CountryApiController implements CountryApi {

    private static final Logger log = Logger.getLogger(CountryApiController.class.getName());

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private Environment env;

    private static Connection connection;

    @org.springframework.beans.factory.annotation.Autowired
    public CountryApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;

        FileHandler fh;
        try {
            fh = new FileHandler("./Country.log", true);
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

    public ResponseEntity<Country> deleteCountry(@ApiParam(value = "",required=true) @PathVariable("country") String country) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Country WHERE id = ?");
                preparedStatement.setString(1, country);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    log.info("Deleted country: " + country);
                    return new ResponseEntity<Country>(HttpStatus.NO_CONTENT);
                }
                log.severe("Error deleting country: " + country);
                return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (SQLException e) {
                log.severe("Error deleting country: " + country + " " + e.getMessage());
                return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of deleteCountry method");
        return new ResponseEntity<Country>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Countries> getCountries() {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT country, country_long, name FROM Country");
                ResultSet rs = preparedStatement.executeQuery();

                Countries countries = new Countries();
                while(rs.next()) {
                    Country country_obj = new Country();
                    country_obj.setCountry(rs.getString(1));
                    country_obj.setCountryLong(rs.getString(2));
                    country_obj.setName(rs.getString(3));
                    countries.add(country_obj);
                }
                log.info("Executed getCountries");
                return new ResponseEntity<Countries>(countries, HttpStatus.OK);
            } catch (SQLException e) {
                log.severe("Error getting Countries " + e.getMessage());
                return new ResponseEntity<Countries>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of getCountries method");
        return new ResponseEntity<Countries>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Country> getCountry(@ApiParam(value = "",required=true) @PathVariable("country") String country) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT country, country_long, name" +
                                " FROM Country WHERE country = ?");
                preparedStatement.setString(1, country);

                ResultSet rs = preparedStatement.executeQuery();
                rs.next();

                Country country_obj = new Country();
                country_obj.setCountry(rs.getString(1));
                country_obj.setCountryLong(rs.getString(2));
                country_obj.setName(rs.getString(3));

                log.info("Executed getCountry id: " + country);
                return new ResponseEntity<Country>(country_obj, HttpStatus.OK);
            } catch (SQLException e) {
                log.severe("Error getting Country id: " + country + " " + e.getMessage());
                return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of getCountry method");
        return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Country> postCountry(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Country body) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Country VALUES (?, ?, ?)");
                preparedStatement.setString(1, body.getCountry());
                preparedStatement.setString(2, body.getCountryLong());
                preparedStatement.setString(3, body.getName());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    log.info("Created new Country");
                    return new ResponseEntity<Country>(HttpStatus.CREATED);
                }
                log.severe("Error creating new Country");
                return new ResponseEntity<Country>(HttpStatus.BAD_REQUEST);
            } catch (SQLException e) {
                log.severe("Error creating new Country " + e.getMessage());
                return new ResponseEntity<Country>(HttpStatus.BAD_REQUEST);
            }
        }
        log.severe("Unknown error end of postCountry method");
        return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Country> updateCountry(@ApiParam(value = "",required=true) @PathVariable("country") String country,@ApiParam(value = "" ,required=true )  @Valid @RequestBody Country body) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Country SET country = ?, country_long = ?, name = ?" +
                                "WHERE country = ?");
                preparedStatement.setString(1, body.getCountry());
                preparedStatement.setString(2, body.getCountryLong());
                preparedStatement.setString(3, body.getName());
                preparedStatement.setString(4, country);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    log.info("Updated Country:" + country);
                    return new ResponseEntity<Country>(HttpStatus.CREATED);
                }
                log.severe("Error updating Country:" + country);
                return new ResponseEntity<Country>(HttpStatus.BAD_REQUEST);
            } catch (SQLException e) {
                log.severe("Error updating Country:" + country + " " + e.getMessage());
                return new ResponseEntity<Country>(HttpStatus.BAD_REQUEST);
            }
        }
        log.severe("Unknown error end of updateCountry method");
        return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> exportCountries() {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("text/plain") || accept.contains("text/csv"))) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT country, country_long, name FROM Country");

                ResultSet rs = preparedStatement.executeQuery();
                StringBuilder sb = new StringBuilder();
                while(rs.next()) {
                    sb.append(rs.getString(1) + ",");
                    sb.append(rs.getString(2) + ",");
                    sb.append(rs.getString(3) + "\n");
                }

                log.info("Executed exportCountries");
                return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);
            } catch (SQLException e) {
                log.severe("Error exporting Countries " + e.getMessage());
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.severe("Unknown error end of exportCountries method");
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> importCountries(@ApiParam(value = "" ,required=true )  @Valid @RequestBody String csv) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            String[] lines = csv.split("\n");
            ResultSet rs;
            PreparedStatement preparedStatement;

            int correct = 0;
            int i = 1;
            String response = "";
            for (String line : lines) {
                String[] parts = line.split(",");
                try {
                    preparedStatement = connection.prepareStatement("INSERT INTO Country VALUES (" +
                            "?, ?, ?)");
                    preparedStatement.setString(1, parts[0]);
                    preparedStatement.setString(2, parts[1]);
                    preparedStatement.setString(3, parts[2]);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected == 0) {
                        response += "Error creating city on line " + i + "\n";
                    } else {
                        correct++;
                    }
                } catch (SQLException e) {
                    response += "Error creating city on line " + i + "\n";
                }
                i++;
            }
            log.info("Imported " + correct + " countries");
            return new ResponseEntity<String>(response, HttpStatus.CREATED);
        }
        log.severe("Unknown error end of importCities method");
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
