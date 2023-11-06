import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.StudentResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHTMLFromFile {
  static List<StudentResult> studentResults = new ArrayList<>();

  public static void main(String[] args) {
    File file = new File("resources/index.html");
    try {
      Document document = Jsoup.parse(file);

      // validation

      validatePasswordField(document);
      validateAgeField(document);
      validateDisplayedCountries(document);
      validateSelectedCountry(document);

      // data extraction

      Element table = document.getElementById("results");

      Element tbody = table.getElementsByTag("tbody").first();

      Elements rows = tbody.children();
      for (Element row : rows) {
        Elements columns = row.children();
        String code = columns.get(0).text();
        String name = columns.get(1).text();
        int age = Integer.parseInt(columns.get(2).text());
        String city = columns.get(3).text();
        String result = columns.get(4).text();

        StudentResult studentResult = new StudentResult(code, name, age, city, result);
        studentResults.add(studentResult);
      }

      long studentsWhoPassed = studentResults.stream().filter(StudentResult::hasPassed).count();
      System.out.println(studentsWhoPassed + " students have passed");
      String studentCode = "3654";
      Optional<StudentResult> studentResultOptional = getStudentResult(studentCode);
      if (studentResultOptional.isPresent()) {
        StudentResult studentResult = studentResultOptional.get();
        System.out.println(studentResult);
      } else {
        System.out.println("student with code " + studentCode + " doesn't exist");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Optional<StudentResult> getStudentResult(String code) {
    return studentResults.stream().filter(r -> r.code().equals(code)).findFirst();
  }

  private static void validatePasswordField(Document document) {
    Element passwordField = document.getElementById("password");
    if (passwordField == null) {
      throw new RuntimeException("no password field");
    }
    String passwordFieldInputType = passwordField.attr("type");
    if (!"password".equals(passwordFieldInputType)) {
      throw new RuntimeException(
          "the password field is of type : " + passwordFieldInputType + " it should be password");
    }
  }

  private static void validateAgeField(Document document) {
    Element ageField = document.getElementById("age");
    if (ageField == null) {
      throw new RuntimeException("no age field");
    }
    String ageFieldInputType = ageField.attr("type");
    if (!"number".equals(ageFieldInputType)) {
      throw new RuntimeException(
          "the age field is of type : " + ageFieldInputType + " it should be number");
    }
    int minValue = Integer.parseInt(ageField.attr("min"));
    if (minValue != 18) {
      throw new RuntimeException("the age field should have a min attribute of 18");
    }
  }

  private static final List<String> allowedCountries = List.of("USA", "Canada", "UK", "France");
  private static final String defaultCountry = "Canada";

  private static void validateDisplayedCountries(Document document) {
    Element country = document.getElementById("country");
    if (country == null) {
      throw new RuntimeException("no country field");
    }
    Elements countries = country.getElementsByTag("option");
    List<String> allCountries = new ArrayList<>();
    for (int i = 0; i < countries.size(); i++) {
      Element option = countries.get(i);
      allCountries.add(option.val());
    }
    if (!allowedCountries.equals(allCountries)) {
      throw new RuntimeException(
          "Error, the country list is not valid, these are the valid countries: "
              + allowedCountries);
    }
  }

  private static void validateSelectedCountry(Document document) {
    Element country = document.getElementById("country");
    if (country == null) {
      throw new RuntimeException("no country field");
    }
    Elements countries = country.getElementsByTag("option");
    String selectedCountry = null;
    for (int i = 0; i < countries.size(); i++) {
      Element option = countries.get(i);
      if (option.hasAttr("selected")) {
        selectedCountry = option.val();
        break;
      }
    }
    if (!defaultCountry.equals(selectedCountry)) {
      throw new RuntimeException(
          defaultCountry + " should be selected by default in the country dropdown");
    }
  }
}
