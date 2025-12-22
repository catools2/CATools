package org.catools.etl.k8s.dao;

import static org.catools.etl.k8s.configs.CEtlKubeConfigs.K8S_SCHEMA;

import java.util.Optional;
import org.catools.etl.k8s.model.CEtlKubeContainer;
import org.catools.etl.k8s.model.CEtlKubePod;

public class CEtlKubePodDao extends CEtlKubeBaseDao {
  /**
   * delete pod by name
   *
   * @param name
   * @return
   */
  public static boolean deletePodByNameIfExists(String name) {
    return doTransaction(
        entityManager -> {
          Optional<CEtlKubePod> oPod =
              entityManager
                  .createNamedQuery("getEtlKubePodByName", CEtlKubePod.class)
                  .setParameter("name", name)
                  .getResultStream()
                  .findFirst();

          if (oPod.isPresent()) {
            CEtlKubePod pod = oPod.get();

            entityManager
                .createNativeQuery(
                    "delete from " + K8S_SCHEMA + ".pod_metadata_mid where pod_name=:podName")
                .setParameter("podName", pod.getName())
                .executeUpdate();

            entityManager
                .createNativeQuery(
                    "delete from " + K8S_SCHEMA + ".pod_container_mid where pod_name=:podName")
                .setParameter("podName", pod.getName())
                .executeUpdate();

            for (CEtlKubeContainer container : pod.getContainers()) {
              entityManager
                  .createNativeQuery(
                      "delete from "
                          + K8S_SCHEMA
                          + ".container_metadata_mid where container_id=:containerId")
                  .setParameter("containerId", container.getId())
                  .executeUpdate();

              entityManager.remove(container);
            }

            entityManager.remove(pod);
          }

          return true;
        });
  }
}
