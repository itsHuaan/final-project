package org.example.final_project.repository;

import org.example.final_project.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAddressRepository extends JpaRepository<AddressEntity, Long> {
  @Query("select  p from AddressEntity p where p.parent_id = :parent_id")
  List<AddressEntity> findByParent_id(Long parent_id);
  boolean existsById(Long id);
  @Query("select  p from AddressEntity p where p.parent_id = :parent_id")
  Optional<AddressEntity> findAddressEntitiesByParentId(Long parent_id);


}