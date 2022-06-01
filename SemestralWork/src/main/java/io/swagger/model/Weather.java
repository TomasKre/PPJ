package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.City;
import io.swagger.model.Forecast;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Weather
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")


public class Weather   {
  @JsonProperty("forecast")
  @Valid
  private Forecast forecast = null;

  @JsonProperty("city")
  private City city = null;

  public Weather forecast(Forecast forecast) {
    this.forecast = forecast;
    return this;
  }

  /**
   * Get forecast
   * @return forecast
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Forecast getForecast() {
    return forecast;
  }

  public void setForecast(Forecast forecast) {
    this.forecast = forecast;
  }

  public Weather city(City city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
  **/
  @ApiModelProperty(value = "")

  @Valid

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Weather weather = (Weather) o;
    return Objects.equals(this.forecast, weather.forecast) &&
        Objects.equals(this.city, weather.city);
  }

  @Override
  public int hashCode() {
    return Objects.hash(forecast, city);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Weather {\n");
    
    sb.append("    forecast: ").append(toIndentedString(forecast)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

