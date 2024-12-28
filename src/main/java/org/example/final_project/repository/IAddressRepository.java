package org.example.final_project.repository;

import org.example.final_project.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAddressRepository extends JpaRepository<AddressEntity, Long> {
    @Query("select  p from AddressEntity p where p.parent_id = :parent_id")
    List<AddressEntity> findByParent_id(Long parent_id);

    boolean existsById(Long id);

    @Query("select  p from AddressEntity p where p.parent_id = :parent_id")
    Optional<AddressEntity> findAddressEntitiesByParentId(Long parent_id);

    @Query(value = """
        WITH RECURSIVE LocationTree AS (
            SELECT id, parent_id
            FROM tbl_address
            WHERE id IN :parentIds
            UNION ALL
            SELECT a.id, a.parent_id
            FROM tbl_address a
            INNER JOIN LocationTree lt ON a.parent_id = lt.id
        )
        SELECT id FROM LocationTree
        WHERE id NOT IN (SELECT DISTINCT parent_id FROM tbl_address WHERE parent_id IS NOT NULL);
        """, nativeQuery = true)
    List<Long> findAllChildLocationIds(@Param("parentIds") List<Long> parentIds);
}
