package io.swagger.api;

import io.swagger.model.Cities;
import io.swagger.model.City;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")

@Controller
public class CityApiController implements CityApi {

    private static final Logger log = LoggerFactory.getLogger(CityApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public CityApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<City> deleteCity(@ApiParam(value = "",required=true) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<City>(objectMapper.readValue("{\"empty\": false}", City.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<City>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Cities> getCities() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Cities>(objectMapper.readValue("{  \"bytes\": [],  \"empty\": true}", Cities.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Cities>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Cities>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<City> getCity(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<City>(objectMapper.readValue("{\"empty\": false}", City.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<City>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<City> postCity(@ApiParam(value = "" ,required=true )  @Valid @RequestBody City body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<City>(objectMapper.readValue("{\"empty\": false}", City.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<City>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<City> updateCity(@ApiParam(value = "",required=true) @PathVariable("id") String id,@ApiParam(value = "" ,required=true )  @Valid @RequestBody City body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<City>(objectMapper.readValue("{\"empty\": false}", City.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<City>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<City>(HttpStatus.NOT_IMPLEMENTED);
    }

}
