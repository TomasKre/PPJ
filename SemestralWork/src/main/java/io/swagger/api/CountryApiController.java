package io.swagger.api;

import io.swagger.model.City;
import io.swagger.model.Countries;
import io.swagger.model.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")

@Controller
public class CountryApiController implements CountryApi {

    private static final Logger log = LoggerFactory.getLogger(CountryApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private Environment env;

    private static Connection connection;

    @org.springframework.beans.factory.annotation.Autowired
    public CountryApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public void initializeConnection() {
        try {
            this.connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"));
        } catch (SQLException e) {
            e.printStackTrace();
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
                    log.error("Deleted country: " + country);
                    return new ResponseEntity<Country>(HttpStatus.NO_CONTENT);
                }
                log.error("Error deleting country: " + country);
                return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (SQLException e) {
                log.error("Error deleting country: " + country, e);
                return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Unknown error end of deleteCountry method");
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
                log.error("Executed getCountries");
                return new ResponseEntity<Countries>(countries, HttpStatus.OK);
            } catch (SQLException e) {
                log.error("Error getting Countries", e);
                return new ResponseEntity<Countries>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Unknown error end of getCountries method");
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

                log.error("Executed getCountry id: " + country);
                return new ResponseEntity<Country>(country_obj, HttpStatus.OK);
            } catch (SQLException e) {
                log.error("Error getting Country id: " + country, e);
                return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Unknown error end of getCountry method");
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
                    log.error("Created new Country");
                    return new ResponseEntity<Country>(HttpStatus.CREATED);
                }
                log.error("Error creating new Country");
                return new ResponseEntity<Country>(HttpStatus.BAD_REQUEST);
            } catch (SQLException e) {
                log.error("Error creating new Country", e);
                return new ResponseEntity<Country>(HttpStatus.BAD_REQUEST);
            }
        }
        log.error("Unknown error end of postCountry method");
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
                    log.error("Updated Country:" + country);
                    return new ResponseEntity<Country>(HttpStatus.CREATED);
                }
                log.error("Error updating Country:" + country);
                return new ResponseEntity<Country>(HttpStatus.BAD_REQUEST);
            } catch (SQLException e) {
                log.error("Error updating Country:" + country, e);
                return new ResponseEntity<Country>(HttpStatus.BAD_REQUEST);
            }
        }
        log.error("Unknown error end of updateCountry method");
        return new ResponseEntity<Country>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
