package app.fish.property;

import app.config.YamlPropertySourceFactory;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties
@PropertySource(value = "fishes.yaml", factory = YamlPropertySourceFactory.class)
public class FishProperties {

  private List<FishesData> fishes;

  @Data
  public static class FishesData {

    private String species;
    private int weight;
    private String type;
    private String imageUrl;
    private int price;
  }

//      @PostConstruct
//    public void test() {
//        System.out.println();
//    }
}
