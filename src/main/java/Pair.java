public class Pair 
{
    private String name;
    private String link;
    private Integer price;
    public Pair(String name, String link, Integer price) {
        this.name = name;
        this.link = link;
        this.price = price;
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
    public String getName() {
        return name;
    }
    public String getLink() {
        return link;
    }
    public Integer getPrice() {
        return price;
    }
}