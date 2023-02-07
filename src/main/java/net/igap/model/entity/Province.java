package net.igap.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "province_tbl")
public class Province implements Serializable {

  @Serial
  private static final long serialVersionUID = 1234567L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String provinceName;

  @OneToMany(mappedBy = "province",cascade = CascadeType.ALL,orphanRemoval = true)
  private Set<City> cities;

  @Column(nullable = false)
  private boolean enable;

  public Province() {
    this.cities = new HashSet<>();
    this.enable = true;
  }
}
