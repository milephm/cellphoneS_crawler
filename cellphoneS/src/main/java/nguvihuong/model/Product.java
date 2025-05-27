package nguvihuong.model;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

public class Product
{
    private String name;
    private String link;
    private String imgURL;
    private Integer price;
    private String rating;
    private String ratingCount;
    private List<String> comments = new ArrayList<>();
    private final Map<String, String> description;

    public Product(String name, String link, String imgURL, Integer price, String rating, String ratingCount, List<String> comments, Map<String, String> description) {
        this.name = name;
        this.link = link;
        this.imgURL = imgURL;
        this.price = price;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.comments = new ArrayList<>();
        this.description = new LinkedHashMap<>(description);
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setImgURL(String imgURL) { this.imgURL = imgURL; }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }
    public void setComments(List<String> comments) {
        this.comments = comments;
    }
    public void setDescription(String key, String value) {
        this.description.put(key, value);
    }


    public String getName() {
        return name;
    }
    public String getLink() {
        return link;
    }
    public String getImgURL() {return imgURL;}
    public Integer getPrice() {
        return price;
    }
    public String getRating() {
        return rating;
    }
    public String getRatingCount() {
        return ratingCount;
    }
    public List<String> getComments() {
        return comments;
    }
    public Map<String, String> getDescription() {
        return description;
    }
}