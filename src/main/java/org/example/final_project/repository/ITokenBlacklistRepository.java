package org.example.final_project.repository;

import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ITokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long>, JpaSpecificationExecutor<TokenBlacklist> {
    Optional<TokenBlacklist> findByToken(String token);
}
