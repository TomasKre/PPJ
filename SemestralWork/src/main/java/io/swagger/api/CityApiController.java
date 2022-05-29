package io.swagger.api;

import io.swagger.model.Cities;
import io.swagger.model.City;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")

// Prepared statement chrání před SQL Injection

@Controller
public class CityApiController implements CityApi {

    private static final Logger log = LoggerFactory.getLogger(CityApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private Environment env;

    private static Connection connection;

    @org.springframework.beans.factory.annotation.Autowired
    public CityApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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

    public ResponseEntity<City> deleteCity(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM City WHERE id = ?");
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    log.error("Deleted city id: " + id);
                    return new ResponseEntity<City>(HttpStatus.NO_CONTENT);
                }
                log.error("Error deleting city id: " + id);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (SQLException e) {
                log.error("Error deleting city id: " + id, e);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Unknown error end of deleteCity method");
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

                log.error("Executed getCities");
                return new ResponseEntity<Cities>(cities, HttpStatus.OK);
            } catch (SQLException e) {
                log.error("Error getting Cities", e);
                return new ResponseEntity<Cities>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Unknown error end of getCities method");
        return new ResponseEntity<Cities>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<City> getCity(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        if (this.connection == null) {
            initializeConnection();
        }
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id, name, country," +
                        " lon, lat FROM City WHERE id = ?");
                preparedStatement.setInt(1, id);

                ResultSet rs = preparedStatement.executeQuery();
                rs.next();

                City city = new City();
                city.setId(rs.getInt(1));
                city.setName(rs.getString(2));
                city.setCountry(rs.getString(3));
                city.setLon(rs.getFloat(4));
                city.setLat(rs.getFloat(5));

                log.error("Executed getCity id: " + id);
                return new ResponseEntity<City>(city, HttpStatus.OK);
            } catch (SQLException e) {
                log.error("Error getting City id: " + id, e);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Unknown error end of getCity method");
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
                        log.error("Created new City");
                        return new ResponseEntity<City>(HttpStatus.CREATED);
                    }
                    log.error("Error creating new City");
                    return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (SQLException e) {
                    log.error("Error creating new City", e);
                    return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (SQLException e) {
                log.error("Country code doesn't exist", e);
                return new ResponseEntity<City>(HttpStatus.BAD_REQUEST);
            }
        }
        log.error("Unknown error end of postCity method");
        return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<City> updateCity(@ApiParam(value = "",required=true) @PathVariable("id") Integer id,@ApiParam(value = "" ,required=true )  @Valid @RequestBody City body) {
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
                            " name = ?, country = ?, lon = ?, lat = ? WHERE id = ?");
                    preparedStatement.setString(1, body.getName());
                    preparedStatement.setString(2, body.getCountry());
                    preparedStatement.setFloat(3, body.getLon());
                    preparedStatement.setFloat(4, body.getLat());
                    preparedStatement.setInt(5, id);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        log.error("Updated City id: " + id);
                        return new ResponseEntity<City>(HttpStatus.CREATED);
                    }
                    log.error("Error updating City id: " + id);
                    return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (SQLException e) {
                    log.error("Error updating City id: " + id, e);
                    return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (SQLException e) {
                log.error("Country code doesn't exist", e);
                return new ResponseEntity<City>(HttpStatus.BAD_REQUEST);
            }
        }
        log.error("Unknown error end of updateCity method");
        return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
