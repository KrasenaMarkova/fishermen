package app.fish.model;

import app.fisherman.model.Fisherman;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Fish {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String species;

  private int weight;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String imageURL;

  @Column(nullable = false)
  private int price;

  private boolean forSale;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private Fisherman owner;
}
