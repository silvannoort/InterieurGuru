import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Product {
    private String productID;
    private String productnaam;
    private String categorie;
    private String affiliateLink;
    private String partnerwebsite;

    public Product(String productID, String productnaam, String categorie, String affiliateLink, String partnerwebsite) {
        this.productID = productID;
        this.productnaam = productnaam;
        this.categorie = categorie;
        this.affiliateLink = affiliateLink;
        this.partnerwebsite = partnerwebsite;
    }

  

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("productID", this.productID);
        json.put("productnaam", this.productnaam);
        json.put("categorie", this.categorie);
        json.put("affiliateLink", this.affiliateLink);
        json.put("partnerwebsite", this.partnerwebsite);
        return json;
    }

    public static Product fromJson(JSONObject json) {
        return new Product(
            json.getString("productID"),
            json.getString("productnaam"),
            json.getString("categorie"),
            json.getString("affiliateLink"),
            json.getString("partnerwebsite")
        );
    }

    public static void voegProductToe(Product product) {
        JSONArray products = leesProducts();
        products.put(product.toJson());
        schrijfProducts(products);
    }

    public void updateProductInfo(String productnaam, String categorie, String affiliateLink, String partnerwebsite) {
        this.productnaam = productnaam;
        this.categorie = categorie;
        this.affiliateLink = affiliateLink;
        this.partnerwebsite = partnerwebsite;

        // update the product in the JSON file
        JSONArray products = leesProducts();
        for (int i = 0; i < products.length(); i++) {
            JSONObject json = products.getJSONObject(i);
            if (json.getString("productID").equals(this.productID)) {
                products.put(i, this.toJson());
                schrijfProducts(products);
                break;
            }
        }
    }

    public static void verwijderProduct(Product product) {
        JSONArray products = leesProducts();
        for (int i = 0; i < products.length(); i++) {
            JSONObject json = products.getJSONObject(i);
            if (json.getString("productID").equals(product.productID)) {
                products.remove(i);
                schrijfProducts(products);
                break;
            }
        }
    }

    private static JSONArray leesProducts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("products.json")));
            return new JSONArray(content);
        } catch (IOException e) {
            
            return new JSONArray();
        }
    }

    private static void schrijfProducts(JSONArray products) {
        try (FileWriter file = new FileWriter("products.json")) {
            file.write(products.toString());
            file.flush();
        } catch (IOException e) {
            
        }
    }
}
