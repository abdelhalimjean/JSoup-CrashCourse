import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHTMLFromString {
  public static void main(String[] args) {
    Document document =
        Jsoup.parse(
            """
              <html>
                <head>
                  <title>First parse</title>
                </head>
                <body>
                  <p>Parsed HTML into a doc.</p>
                  <p id="someId">Second paragraph</p>
                  <p>A 3rd paragraph about nothing</p>
                </body>
              </html>
            """);

    String title = document.title();
    System.out.println(title);
    Elements paragraphs = document.getElementsByTag("p");
    System.out.println("1st paragraph : " + paragraphs.first());
    System.out.println("last paragraph : " + paragraphs.last());
    System.out.println("number of paragraph : " + paragraphs.size());
    Element specificElementWithId = document.getElementById("someId");
    System.out.println(specificElementWithId);
  }
}
