package org.dep.imeiregistry.repository;

import org.dep.imeiregistry.model.Imei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImeiRepository extends JpaRepository<Imei, Long> {

    Optional<Imei> findByImeiNumber(String imeiNumber);
}
