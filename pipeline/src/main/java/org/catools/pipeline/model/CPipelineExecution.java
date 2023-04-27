package org.catools.pipeline.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "execution")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CPipelineExecution implements Serializable {

  @Serial
  private static final long serialVersionUID = 6051874058285613707L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "packageName", nullable = false)
  private String packageName;

  @Column(name = "className", nullable = false)
  private String className;

  @Column(name = "methodName", nullable = false)
  private String methodName;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "startTime", nullable = false)
  private Date startTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "endTime", nullable = false)
  private Date endTime;

  @ManyToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      targetEntity = CPipelineStatus.class)
  @JoinColumn(
      name = "status_id",
      referencedColumnName = "id",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_EXECUTION_STATUS"))
  private CPipelineStatus status;

  @ManyToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      targetEntity = CPipelineUser.class)
  @JoinColumn(
      name = "executor_id",
      referencedColumnName = "name",
      foreignKey = @ForeignKey(name = "FK_EXECUTION_USER"))
  private CPipelineUser executor;

  @ManyToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      targetEntity = CPipeline.class)
  @JoinColumn(
      name = "pipeline_id",
      referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "FK_EXECUTION_PIPELINE"))
  private CPipeline pipeline;

  @ManyToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      targetEntity = CPipelineExecutionMetaData.class)
  @JoinTable(
      name = "execution_metadata_mid",
      joinColumns = {@JoinColumn(name = "execution_id")},
      inverseJoinColumns = {@JoinColumn(name = "metadata_id")}
  )
  private List<CPipelineExecutionMetaData> metadata = new ArrayList<>();
}
