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
 * Wind
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")


public class Wind   {
  @JsonProperty("speed")
  private Float speed = null;

  @JsonProperty("deg")
  private Integer deg = null;

  @JsonProperty("gust")
  private Float gust = null;

  public Wind speed(Float speed) {
    this.speed = speed;
    return this;
  }

  /**
   * Get speed
   * @return speed
  **/
  @ApiModelProperty(value = "")


  public Float getSpeed() {
    return speed;
  }

  public void setSpeed(Float speed) {
    this.speed = speed;
  }

  public Wind deg(Integer deg) {
    this.deg = deg;
    return this;
  }

  /**
   * Get deg
   * @return deg
  **/
  @ApiModelProperty(value = "")


  public Integer getDeg() {
    return deg;
  }

  public void setDeg(Integer deg) {
    this.deg = deg;
  }

  public Wind gust(Float gust) {
    this.gust = gust;
    return this;
  }

  /**
   * Get gust
   * @return gust
  **/
  @ApiModelProperty(value = "")


  public Float getGust() {
    return gust;
  }

  public void setGust(Float gust) {
    this.gust = gust;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Wind wind = (Wind) o;
    return Objects.equals(this.speed, wind.speed) &&
        Objects.equals(this.deg, wind.deg) &&
        Objects.equals(this.gust, wind.gust);
  }

  @Override
  public int hashCode() {
    return Objects.hash(speed, deg, gust);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Wind {\n");
    
    sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
    sb.append("    deg: ").append(toIndentedString(deg)).append("\n");
    sb.append("    gust: ").append(toIndentedString(gust)).append("\n");
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

