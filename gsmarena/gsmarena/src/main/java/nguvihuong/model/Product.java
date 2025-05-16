package nguvihuong.model;

import java.util.Map;
import java.util.LinkedHashMap;

public class Product
{
    private String name;
    private String link;
    private String imgUrl;
    private final Map<String, String> info;
    private String brand;

    public Product(String name, String link, String imgUrl, Map<String, String> info, String brand) {
        this.name = name;
        this.link = link;
        this.imgUrl = imgUrl;
        this.info = new LinkedHashMap<>(info);
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setInfo(String key, String value) {
        this.info.put(key, value);
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }
    public String getLink() {
        return link;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public Map<String, String> getInfo() {
        return info;
    }
    public String getBrand() {
        return brand;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("link", link);
        map.put("imgUrl", imgUrl);
        map.put("info", info);
        map.put("brand", brand);
        return map;
    }
}