package xyz.warringtons.daterandevu.Modules;

import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Warrington on 8/12/17.
 */
@Entity
@IgnoreExtraProperties
public class Weather {

    @Id
    private Long id;

    private String weather;

    private String description;

    private String temperature;

    @Generated(hash = 615060328)
    public Weather(Long id, String weather, String description, String temperature,
            String weatherIcon) {
        this.id = id;
        this.weather = weather;
        this.description = description;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
    }

    @Generated(hash = 556711069)
    public Weather() {
    }

    public String weatherIcon;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherIcon() {
        return this.weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
}
