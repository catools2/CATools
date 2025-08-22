package org.catools.etl.tms.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;


@NamedQuery(name = "getEtlMetaDataByNameAndValue", query = "FROM CEtlItemMetaData where name=:name and value=:value")
@Entity
@Table(name = "metadata", schema = "tms")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "metadata")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CEtlItemMetaData implements Serializable {

  @Serial
  private static final long serialVersionUID = 6067874018185613707L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "value", length = 100)
  private String value;

  public CEtlItemMetaData(String name, String value) {
    this.name = StringUtils.substring(name, 0, 100);
    this.value = StringUtils.substring(value, 0, 100);
  }
}
