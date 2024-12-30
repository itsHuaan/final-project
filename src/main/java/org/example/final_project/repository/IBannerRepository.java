package org.example.final_project.repository;

import org.example.final_project.entity.BannerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IBannerRepository extends JpaRepository<BannerEntity, Long> {

    @Query("select b from BannerEntity b order by b.timeCreate desc")
    Page<BannerEntity> findBannerPage(Pageable pageable);

    @Query("select b from BannerEntity b where b.isActive != 3")
    List<BannerEntity> findAllBanner();

    @Query("select b from BannerEntity b where b.isActive = 1")
    Optional<BannerEntity> bannerIsActive();

    @Query("SELECT b FROM BannerEntity b WHERE b.createEnd < :currentTime AND b.isActive <> 3")
    List<BannerEntity> findExpiredBanners(@Param("currentTime") LocalDateTime currentTime);

    @Query("select b from BannerEntity b where b.shopId = :shopId")
    List<BannerEntity> listBannerByShopId(@Param("shopId") Long shopId);

    @Modifying
    @Query("UPDATE BannerEntity b SET b.isActive = 0")
    void deactivateAllBanners();

}
