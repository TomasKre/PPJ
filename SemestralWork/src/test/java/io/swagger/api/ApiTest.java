package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class ApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ApiOriginFilter apiFilter;

    @MockBean
    CityApiController cityApi;

    @MockBean
    CountryApiController countryApi;

    @MockBean
    WeatherApiController weatherApi;

    //@MockBean
    //FetchDataService fetchDataService;

    @Test
    void addCity() throws Exception {
        City city = new City();
        city.setCountry("CZ");
        city.setLat((float)14.007);
        city.setLon((float)50.001);
        city.setName("Dummy city");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(city);

        mvc.perform(post("/city").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andExpect(status().isOk());
    }

    @Test
    void getCity() throws Exception {
        City city = new City();
        city.setId(3);
        city.setName("Praha");
        city.setCountry("CZ");
        city.setLon((float)50.07366);
        city.setLat((float)14.41854);

        ResponseEntity<City> response = new ResponseEntity<>(city, HttpStatus.OK);

        given(cityApi.getCity("Praha")).willReturn(response);

        mvc.perform(get("/city/Praha").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                /*.andExpect(jsonPath("$.country").value("CZ"))
                .andExpect(jsonPath("$.lon").value(50.07366))
                .andExpect(jsonPath("$.lat").value(14.41854))*/;
    }

    @Test
    void getCities() throws Exception {
        Cities cities = new Cities();
        City city = new City();
        city.setId(3);
        city.setName("Praha");
        city.setCountry("CZ");
        city.setLon((float)50.07366);
        city.setLat((float)14.41854);
        cities.add(city);

        ResponseEntity<Cities> response = new ResponseEntity<>(cities, HttpStatus.OK);

        given(cityApi.getCities()).willReturn(response);

        mvc.perform(get("/city")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                /*.andExpect(jsonPath("$[0].country").value("CZ"))
                .andExpect(jsonPath("$[0].lon").value(50.07366))
                .andExpect(jsonPath("$[0].lat").value(14.41854))*/;
    }

    @Test
    void deleteCity() throws Exception {
        mvc.perform(delete("/city/Praha")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateCity() throws Exception {
        City city = new City();
        city.setCountry("CZ");
        city.setLat((float)14.007);
        city.setLon((float)50.001);
        city.setName("Dummy city");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(city);

        mvc.perform(put("/city/Praha")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andExpect(status().isOk());
    }

    @Test
    void addCountry() throws Exception {
        Country country = new Country();
        country.setCountry("DC");
        country.setCountryLong("DCO");
        country.setName("Dummy country");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(country);

        mvc.perform(post("/country").contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andExpect(status().isOk());
    }

    @Test
    void getCountry() throws Exception {
        Country country = new Country();
        country.setCountry("CZ");
        country.setCountryLong("CZE");
        country.setName("Česko");

        ResponseEntity<Country> response = new ResponseEntity<>(country, HttpStatus.OK);

        given(countryApi.getCountry("CZ")).willReturn(response);

        ResultActions result = mvc.perform(get("/country/CZ"))
                .andExpect(status().isOk())
                /*.andExpect(jsonPath("$.country").value("CZ"))
                .andExpect(jsonPath("$.country_long").value("CZE"))
                .andExpect(jsonPath("$.name").value("Česko"))*/;
    }

    @Test
    void getCountries() throws Exception {
        Countries countries = new Countries();
        Country country = new Country();
        country.setCountry("CZ");
        country.setCountryLong("CZE");
        country.setName("Česko");
        countries.add(country);

        ResponseEntity<Countries> response = new ResponseEntity<>(countries, HttpStatus.OK);

        given(countryApi.getCountries()).willReturn(response);

        mvc.perform(get("/country")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                /*.andExpect(jsonPath("$[0].country").value("CZ"))
                .andExpect(jsonPath("$[0].country_long").value("CZE"))
                .andExpect(jsonPath("$[0].name").value("Česko"))*/;


    }

    @Test
    void deleteCountry() throws Exception {
        mvc.perform(delete("/country/DC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateCountry() throws Exception {
        Country country = new Country();
        country.setCountry("DC");
        country.setCountryLong("DCO");
        country.setName("Dummy country");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(country);

        mvc.perform(put("/country/DC")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk());
    }

}