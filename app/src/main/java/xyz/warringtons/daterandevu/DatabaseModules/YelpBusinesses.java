package xyz.warringtons.daterandevu.Modules;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Warrington on 2/19/18.
 */
@Entity
public class YelpBusinesses {
    @Id
    private Long id;

    private String name;

    private String image_url;

    private Boolean is_closed;

    private String url;

    private String rate;

    private String price;

    @Generated(hash = 651483940)
    public YelpBusinesses(Long id, String name, String image_url, Boolean is_closed, String url, String rate, String price) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.is_closed = is_closed;
        this.url = url;
        this.rate = rate;
        this.price = price;
    }

    @Generated(hash = 1048071278)
    public YelpBusinesses() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Boolean getIs_closed() {
        return is_closed;
    }

    public void setIs_closed(Boolean is_closed) {
        this.is_closed = is_closed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
