package org.example.final_project.repository;

import org.example.final_project.entity.UserShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IShippingAddressRepository extends JpaRepository<UserShippingAddressEntity, Long>, JpaSpecificationExecutor<UserShippingAddressEntity> {
}
