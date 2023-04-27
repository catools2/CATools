package org.catools.pipeline.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@NamedQueries({
    @NamedQuery(name = "getLastByName", query = "FROM CPipeline where id = (Select max(id) FROM CPipeline where name=:name)")
})
@Table(name = "pipeline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pipeline")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CPipeline implements Serializable {

  @Serial
  private static final long serialVersionUID = 6051874043285613707L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "description", length = 300, nullable = false)
  private String description;

  @Column(name = "start_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;

  @Column(name = "end_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  @ManyToOne(
      cascade = CascadeType.ALL,
      targetEntity = CPipelineProject.class,
      fetch = FetchType.LAZY)
  @JoinColumn(name = "project_code",
      referencedColumnName = "code",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_PIPELINE_PROJECT"))
  private CPipelineProject project;

  @ManyToOne(
      cascade = CascadeType.ALL,
      targetEntity = CPipelineEnvironment.class,
      fetch = FetchType.LAZY)
  @JoinColumn(
      name = "environment_code",
      referencedColumnName = "code",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_PIPELINE_ENVIRONMENT"))
  private CPipelineEnvironment environment;

  @ManyToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      targetEntity = CPipelineMetaData.class)
  @JoinTable(
      name = "pipeline_metadata_mid",
      joinColumns = {@JoinColumn(name = "pipeline_id")},
      inverseJoinColumns = {@JoinColumn(name = "metadata_id")}
  )
  private List<CPipelineMetaData> metadata = new ArrayList<>();
}
