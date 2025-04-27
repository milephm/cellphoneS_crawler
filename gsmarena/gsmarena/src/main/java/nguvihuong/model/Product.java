package nguvihuong.model;

import java.util.Map;
import java.util.LinkedHashMap;

public class Product
{
    private String name;
    private String link;
    private final Map<String, String> info;

    public Product(String name, String link, Map<String, String> info) {
        this.name = name;
        this.link = link;
        this.info = new LinkedHashMap<>(info);
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setInfo(String key, String value) {
        this.info.put(key, value);
    }

    public String getName() {
        return name;
    }
    public String getLink() {
        return link;
    }
    public Map<String, String> getInfo() {
        return info;
    }
}