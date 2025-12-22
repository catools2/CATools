package org.catools.etl.k8s.model;

import static org.catools.etl.k8s.configs.CEtlKubeConfigs.K8S_SCHEMA;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@NamedQuery(
    name = "getEtlKubePodMetaData",
    query = "FROM CEtlKubePodMetaData where type=:type and name=:name and value=:value")
@Entity
@Table(name = "pod_metadata", schema = K8S_SCHEMA)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pod_metadata")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CEtlKubePodMetaData implements Serializable {

  @Serial private static final long serialVersionUID = 8561370606787401817L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "type", length = 50, nullable = false)
  private String type;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "value", length = 1000, nullable = false)
  private String value;

  public CEtlKubePodMetaData(String type, String name, String value) {
    this.type = StringUtils.substring(type, 0, 50);
    this.name = StringUtils.substring(name, 0, 100);
    this.value = StringUtils.substring(value, 0, 1000);
  }
}
