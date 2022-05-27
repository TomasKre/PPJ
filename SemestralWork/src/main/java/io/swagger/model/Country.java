package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Country
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")


public class Country   {
  @JsonProperty("country")
  private String country = null;

  @JsonProperty("country_long")
  private String countryLong = null;

  @JsonProperty("name")
  private String name = null;

  public Country country(String country) {
    this.country = country;
    return this;
  }

  /**
   * Get country
   * @return country
  **/
  @ApiModelProperty(value = "")


  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Country countryLong(String countryLong) {
    this.countryLong = countryLong;
    return this;
  }

  /**
   * Get countryLong
   * @return countryLong
  **/
  @ApiModelProperty(value = "")


  public String getCountryLong() {
    return countryLong;
  }

  public void setCountryLong(String countryLong) {
    this.countryLong = countryLong;
  }

  public Country name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Country country = (Country) o;
    return Objects.equals(this.country, country.country) &&
        Objects.equals(this.countryLong, country.countryLong) &&
        Objects.equals(this.name, country.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(country, countryLong, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Country {\n");
    
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    countryLong: ").append(toIndentedString(countryLong)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

