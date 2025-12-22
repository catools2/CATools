package org.catools.etl.k8s.model;

import static org.catools.etl.k8s.configs.CEtlKubeConfigs.K8S_SCHEMA;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@NamedQuery(name = "getEtlKubeProjectByName", query = "FROM CEtlKubeProject where name=:name")
@Entity
@Table(name = "project", schema = K8S_SCHEMA)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CEtlKubeProject implements Serializable {

  public static final CEtlKubeProject UNSET = new CEtlKubeProject("UNSET");
  @Serial private static final long serialVersionUID = 1370760698740181856L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 100, unique = true, nullable = false)
  private String name;

  public CEtlKubeProject(String name) {
    this.name = StringUtils.substring(name, 0, 100);
  }
}
