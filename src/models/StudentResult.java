package models;

public record StudentResult(String code, String name, int age, String city, String result) {
  public boolean hasPassed() {
    return "pass".equalsIgnoreCase(result);
  }
}
