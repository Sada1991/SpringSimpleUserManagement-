package net.igap.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account_tbl")
public class Account implements Serializable {

  @Serial
  private static final long serialVersionUID = 1234567L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private Person person;

  @OneToOne
  private Address address;

  @CreationTimestamp
  private LocalDate createdDate;

  @Column(nullable = false ,updatable = false)
  private String userName;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean enable;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "account_roles",
  joinColumns = @JoinColumn(name = "account_id",referencedColumnName = "id"),
  inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
  private Set<Role> roles;

  public Account() {
    this.enable = true;
  }
}
