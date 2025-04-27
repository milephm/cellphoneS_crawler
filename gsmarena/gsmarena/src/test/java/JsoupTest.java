import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupTest {
    public static void main(String[] args) {
        try {
            String url = "https://www.gsmarena.com/google_pixel_9a-13478.php";
            Document doc = Jsoup.connect(url).get();

            // Check if we can find the spec tables
            Elements tables = doc.select("#specs-list table");
            System.out.println("Found " + tables.size() + " spec tables");

            // Check if we can find the title
            String title = doc.select(".specs-phone-name-title").text();
            System.out.println("Found title: " + title);

            // Look for some specs
            Elements specs = doc.select("td.ttl, td.nfo");
            System.out.println("Found " + specs.size() + " spec elements");

            // Show some sample specs
            for (int i = 0; i < Math.min(10, specs.size()); i++) {
                System.out.println(specs.get(i).text());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}