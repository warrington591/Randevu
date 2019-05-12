package xyz.warringtons.daterandevu.Modules;

import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by Warrington on 8/12/17.
 */

@Entity
@IgnoreExtraProperties
public class Activities {

    @Id(autoincrement = true)
    private Long id;

    private String firebaseId;

    private String CategoryId;

    private String ActivityName;

    private String WeatherCondition;

    private Boolean selected;

    private Boolean deleted;

    private String location;

    private String picDatabaseId;

    private String yelpKeyword="";

    @NotNull
    private Boolean complete;

    public Activities(Long id, String CategoryId, String ActivityName,
            String WeatherCondition, Boolean selected, Boolean deleted,
            String location, String yelpKeyword) {
        this.id = id;
        this.CategoryId = CategoryId;
        this.ActivityName = ActivityName;
        this.WeatherCondition = WeatherCondition;
        this.selected = selected;
        this.deleted = deleted;
        this.location = location;
        this.yelpKeyword = yelpKeyword;
    }

    public Activities() {
    }

    @Keep
    public Activities(Long id, String firebaseId, String CategoryId,
            String ActivityName, String WeatherCondition, Boolean selected,
            Boolean deleted, String location, String picDatabaseId,
            @NotNull Boolean complete) {
        this.id = id;
        this.firebaseId = firebaseId;
        this.CategoryId = CategoryId;
        this.ActivityName = ActivityName;
        this.WeatherCondition = WeatherCondition;
        this.selected = selected;
        this.deleted = deleted;
        this.location = location;
        this.picDatabaseId = picDatabaseId;
        this.complete = complete;
    }

    @Generated(hash = 518670365)
    public Activities(Long id, String firebaseId, String CategoryId,
            String ActivityName, String WeatherCondition, Boolean selected,
            Boolean deleted, String location, String picDatabaseId,
            String yelpKeyword, @NotNull Boolean complete) {
        this.id = id;
        this.firebaseId = firebaseId;
        this.CategoryId = CategoryId;
        this.ActivityName = ActivityName;
        this.WeatherCondition = WeatherCondition;
        this.selected = selected;
        this.deleted = deleted;
        this.location = location;
        this.picDatabaseId = picDatabaseId;
        this.yelpKeyword = yelpKeyword;
        this.complete = complete;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryId() {
        return this.CategoryId;
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getActivityName() {
        return this.ActivityName;
    }

    public void setActivityName(String ActivityName) {
        this.ActivityName = ActivityName;
    }

    public String getWeatherCondition() {
        return this.WeatherCondition;
    }

    public void setWeatherCondition(String WeatherCondition) {
        this.WeatherCondition = WeatherCondition;
    }

    public String getYelpKeyword() {
        return yelpKeyword;
    }

    public void setYelpKeyword(String yelpKeyword) {
        this.yelpKeyword = yelpKeyword;
    }

    public Boolean wasSelected() {
        return this.selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getSelected() {
        return this.selected;
    }

    public Boolean getComplete() {
        if(this.complete== null)
            this.complete = false;
        return this.complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getPicDatabaseId() {
        return picDatabaseId;
    }

    public void setPicDatabaseId(String picDatabaseId) {
        this.picDatabaseId = picDatabaseId;
    }
}
