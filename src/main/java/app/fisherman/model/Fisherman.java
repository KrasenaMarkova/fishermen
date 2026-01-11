package app.fisherman.model;

import app.fish.model.Fish;
import jakarta.persistence.PrePersist;
import app.fisherman.enums.Experience;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class Fisherman {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  private String imageUrl;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Experience experience;

  @Column(nullable = false)
  private int cash;

  @Column(nullable = false)
  private LocalDateTime createdOn;

  @Column(nullable = false)
  private LocalDateTime updatedOn;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Fish> fishes = new ArrayList<>();

  @PrePersist
  public void prePersist() {
    this.cash = experience != null ? experience.getInitialCash() : 0;
    this.createdOn = LocalDateTime.now();
    this.updatedOn = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedOn = LocalDateTime.now();
  }
}
