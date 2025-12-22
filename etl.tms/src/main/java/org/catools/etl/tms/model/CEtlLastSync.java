package org.catools.etl.tms.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "last_sync", schema = "tms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CEtlLastSync implements Serializable {

  @Serial private static final long serialVersionUID = 6051875018178513707L;

  @Id
  @Column(length = 300, name = "key")
  private String key;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "sync_date")
  private Date syncDate;
}
