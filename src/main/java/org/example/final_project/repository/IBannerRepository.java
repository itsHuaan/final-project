package org.example.final_project.repository;

import org.example.final_project.entity.BannerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IBannerRepository extends JpaRepository<BannerEntity, Long> {

    @Query("select b from BannerEntity b order by b.timeCreate desc")
    Page<BannerEntity> findBannerPage(Pageable pageable);

    @Query("select b from BannerEntity b where b.isActive != 3")
    List<BannerEntity> findAllBanner();

    @Query("select b from BannerEntity b where b.isActive == 1")
    Optional<BannerEntity> bannerIsActive();

    @Query("SELECT b FROM BannerEntity b WHERE b.createEnd < :currentTime AND b.isActive <> 3")
    List<BannerEntity> findExpiredBanners(@Param("currentTime") LocalDateTime currentTime);


}
