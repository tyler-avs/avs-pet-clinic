package com.astroviking.avspetclinic.repositories;

import com.astroviking.avspetclinic.model.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends CrudRepository<Owner, Long> {
  Owner findByLastName(String lastName);
}