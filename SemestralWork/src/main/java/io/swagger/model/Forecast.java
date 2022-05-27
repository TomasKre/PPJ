package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Wind;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Forecast
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-05-27T22:30:01.988Z")


public class Forecast   {
  @JsonProperty("dt")
  private Integer dt = null;

  @JsonProperty("dt_txt")
  private String dtTxt = null;

  @JsonProperty("temp")
  private Float temp = null;

  @JsonProperty("feels_like")
  private Float feelsLike = null;

  @JsonProperty("temp_min")
  private Float tempMin = null;

  @JsonProperty("temp_max")
  private Float tempMax = null;

  @JsonProperty("pressure")
  private Integer pressure = null;

  @JsonProperty("sea_level")
  private Integer seaLevel = null;

  @JsonProperty("grnd_level")
  private Integer grndLevel = null;

  @JsonProperty("humidity")
  private Integer humidity = null;

  @JsonProperty("wind")
  private Wind wind = null;

  @JsonProperty("visibility")
  private Integer visibility = null;

  public Forecast dt(Integer dt) {
    this.dt = dt;
    return this;
  }

  /**
   * Get dt
   * @return dt
  **/
  @ApiModelProperty(value = "")


  public Integer getDt() {
    return dt;
  }

  public void setDt(Integer dt) {
    this.dt = dt;
  }

  public Forecast dtTxt(String dtTxt) {
    this.dtTxt = dtTxt;
    return this;
  }

  /**
   * Get dtTxt
   * @return dtTxt
  **/
  @ApiModelProperty(value = "")


  public String getDtTxt() {
    return dtTxt;
  }

  public void setDtTxt(String dtTxt) {
    this.dtTxt = dtTxt;
  }

  public Forecast temp(Float temp) {
    this.temp = temp;
    return this;
  }

  /**
   * Get temp
   * @return temp
  **/
  @ApiModelProperty(value = "")


  public Float getTemp() {
    return temp;
  }

  public void setTemp(Float temp) {
    this.temp = temp;
  }

  public Forecast feelsLike(Float feelsLike) {
    this.feelsLike = feelsLike;
    return this;
  }

  /**
   * Get feelsLike
   * @return feelsLike
  **/
  @ApiModelProperty(value = "")


  public Float getFeelsLike() {
    return feelsLike;
  }

  public void setFeelsLike(Float feelsLike) {
    this.feelsLike = feelsLike;
  }

  public Forecast tempMin(Float tempMin) {
    this.tempMin = tempMin;
    return this;
  }

  /**
   * Get tempMin
   * @return tempMin
  **/
  @ApiModelProperty(value = "")


  public Float getTempMin() {
    return tempMin;
  }

  public void setTempMin(Float tempMin) {
    this.tempMin = tempMin;
  }

  public Forecast tempMax(Float tempMax) {
    this.tempMax = tempMax;
    return this;
  }

  /**
   * Get tempMax
   * @return tempMax
  **/
  @ApiModelProperty(value = "")


  public Float getTempMax() {
    return tempMax;
  }

  public void setTempMax(Float tempMax) {
    this.tempMax = tempMax;
  }

  public Forecast pressure(Integer pressure) {
    this.pressure = pressure;
    return this;
  }

  /**
   * Get pressure
   * @return pressure
  **/
  @ApiModelProperty(value = "")


  public Integer getPressure() {
    return pressure;
  }

  public void setPressure(Integer pressure) {
    this.pressure = pressure;
  }

  public Forecast seaLevel(Integer seaLevel) {
    this.seaLevel = seaLevel;
    return this;
  }

  /**
   * Get seaLevel
   * @return seaLevel
  **/
  @ApiModelProperty(value = "")


  public Integer getSeaLevel() {
    return seaLevel;
  }

  public void setSeaLevel(Integer seaLevel) {
    this.seaLevel = seaLevel;
  }

  public Forecast grndLevel(Integer grndLevel) {
    this.grndLevel = grndLevel;
    return this;
  }

  /**
   * Get grndLevel
   * @return grndLevel
  **/
  @ApiModelProperty(value = "")


  public Integer getGrndLevel() {
    return grndLevel;
  }

  public void setGrndLevel(Integer grndLevel) {
    this.grndLevel = grndLevel;
  }

  public Forecast humidity(Integer humidity) {
    this.humidity = humidity;
    return this;
  }

  /**
   * Get humidity
   * @return humidity
  **/
  @ApiModelProperty(value = "")


  public Integer getHumidity() {
    return humidity;
  }

  public void setHumidity(Integer humidity) {
    this.humidity = humidity;
  }

  public Forecast wind(Wind wind) {
    this.wind = wind;
    return this;
  }

  /**
   * Get wind
   * @return wind
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Wind getWind() {
    return wind;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
  }

  public Forecast visibility(Integer visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
  **/
  @ApiModelProperty(value = "")


  public Integer getVisibility() {
    return visibility;
  }

  public void setVisibility(Integer visibility) {
    this.visibility = visibility;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Forecast forecast = (Forecast) o;
    return Objects.equals(this.dt, forecast.dt) &&
        Objects.equals(this.dtTxt, forecast.dtTxt) &&
        Objects.equals(this.temp, forecast.temp) &&
        Objects.equals(this.feelsLike, forecast.feelsLike) &&
        Objects.equals(this.tempMin, forecast.tempMin) &&
        Objects.equals(this.tempMax, forecast.tempMax) &&
        Objects.equals(this.pressure, forecast.pressure) &&
        Objects.equals(this.seaLevel, forecast.seaLevel) &&
        Objects.equals(this.grndLevel, forecast.grndLevel) &&
        Objects.equals(this.humidity, forecast.humidity) &&
        Objects.equals(this.wind, forecast.wind) &&
        Objects.equals(this.visibility, forecast.visibility);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dt, dtTxt, temp, feelsLike, tempMin, tempMax, pressure, seaLevel, grndLevel, humidity, wind, visibility);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Forecast {\n");
    
    sb.append("    dt: ").append(toIndentedString(dt)).append("\n");
    sb.append("    dtTxt: ").append(toIndentedString(dtTxt)).append("\n");
    sb.append("    temp: ").append(toIndentedString(temp)).append("\n");
    sb.append("    feelsLike: ").append(toIndentedString(feelsLike)).append("\n");
    sb.append("    tempMin: ").append(toIndentedString(tempMin)).append("\n");
    sb.append("    tempMax: ").append(toIndentedString(tempMax)).append("\n");
    sb.append("    pressure: ").append(toIndentedString(pressure)).append("\n");
    sb.append("    seaLevel: ").append(toIndentedString(seaLevel)).append("\n");
    sb.append("    grndLevel: ").append(toIndentedString(grndLevel)).append("\n");
    sb.append("    humidity: ").append(toIndentedString(humidity)).append("\n");
    sb.append("    wind: ").append(toIndentedString(wind)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
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

