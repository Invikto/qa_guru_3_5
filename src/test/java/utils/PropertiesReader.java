package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

  private final String propertiesSet;

  public PropertiesReader(String propertiesSet) {
    this.propertiesSet = propertiesSet;
  }

  private static Properties loadProperties(String propertiesSet) {
    Properties properties = new Properties();

    try {
      if (!"credentials".equals(propertiesSet)) {
        throw new IllegalStateException("Unexpected value: " + propertiesSet);
      }
      properties.load(new FileReader(String.format("./src/test/resources/%s.properties", propertiesSet)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return properties;
  }

  public String getProperty(String key) {
    return loadProperties(propertiesSet).getProperty(key);
  }

}
