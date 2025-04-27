package nguvihuong.model;

import java.util.Map;
import java.util.LinkedHashMap;

public class Product
{
    private String name;
    private String link;
    private Integer price;
    private final Map<String, String> description;

    public Product(String name, String link, Integer price, Map<String, String> description) {
        this.name = name;
        this.link = link;
        this.price = price;
        this.description = new LinkedHashMap<>(description);
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setPrice(Integer price) {
        this.price = price;
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
    public Integer getPrice() {
        return price;
    }
    public Map<String, String> getDescription() {
        return description;
    }
}