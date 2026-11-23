package org.dep.imeiRegistory.Repository;

import org.dep.imeiRegistory.model.Imei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImeiRepository extends JpaRepository<Imei, Long> {
    Optional<Imei> findByImeiNumber(String imeiNumber);
}
