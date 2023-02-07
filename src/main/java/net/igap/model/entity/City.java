package net.igap.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "city_tbl")
public class City implements Serializable {

  @Serial
  private static final long serialVersionUID = 1234567L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String cityName;

  @Column(nullable = false)
  private boolean enable;

  @ManyToOne(fetch = FetchType.LAZY)
  private Province province;

  public City() {
    this.enable = true;
  }
}
