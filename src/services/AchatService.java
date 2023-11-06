package services;

import java.util.List;
import models.Produit;

public class AchatService {
  private static final List<String> produitsInteressants =
      List.of("Dell", "HP", "Mac", "Macbook", "iMac", "Asus");

  public static boolean isProduitInteressant(Produit produit) {
    for (String produitInteressant : produitsInteressants) {
      if (produit.titre().contains(produitInteressant)) {
        return true;
      }
    }
    return false;
  }

  public static void notifierProduitInteressant(Produit produit) {
    System.out.println("Le produit : " + produit.titre() + " peut vous intéresser");
  }
}
