import java.io.IOException;
import models.Produit;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import services.AchatService;

public class ParseHTMLFromUrl {
  public static void main(String[] args) {
    try {
      Connection connection =
          Jsoup.connect("https://www.voursa.com/index.cfm?ctid=1").timeout(20000);
      Document document = connection.get();
      Elements produits =
          document.getElementsByAttributeValueStarting("href", "/annonces.cfm?pdtid=");

      for (Element element : produits) {
        if (!element.getElementById("vendue_loue").text().isEmpty()) {
          // si l'élément a été vendu, on passe vers le suivant
          continue;
        }
        String titre = element.getElementById("titre").text();
        String url = element.attr("href");
        String prix = element.getElementById("prix").text();
        Element image = element.getElementsByTag("img").first();
        String imageUrl = image.attr("src");
        Produit produit = new Produit(titre, url, prix, imageUrl);
        if (AchatService.isProduitInteressant(produit)) {
          // si le produit nous intéresse, on envoie une notification (mail, sms ...)
          AchatService.notifierProduitInteressant(produit);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
