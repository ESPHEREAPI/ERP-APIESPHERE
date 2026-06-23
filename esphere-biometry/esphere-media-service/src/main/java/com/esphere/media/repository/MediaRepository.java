package com.esphere.media.repository;

import com.esphere.media.entity.MediaPrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<MediaPrestation, Integer> {

    @Query("SELECT m FROM MediaPrestation m WHERE m.visiteId = :visiteId AND m.supprime = '-1'")
    List<MediaPrestation> findByVisite(@Param("visiteId") String visiteId);

    @Query("SELECT m FROM MediaPrestation m WHERE m.codeAdherent = :codeAdherent AND m.supprime = '-1' ORDER BY m.dateUpload DESC")
    List<MediaPrestation> findByAdherent(@Param("codeAdherent") String codeAdherent);

    @Query("SELECT m FROM MediaPrestation m WHERE m.prestataireId = :prestataireId AND m.supprime = '-1' ORDER BY m.dateUpload DESC")
    List<MediaPrestation> findByPrestataire(@Param("prestataireId") String prestataireId);

    @Query("SELECT m FROM MediaPrestation m WHERE m.demandeParSs = true AND m.supprime = '-1' ORDER BY m.dateUpload DESC")
    List<MediaPrestation> findDemandesParSs();

    @Query("SELECT m FROM MediaPrestation m WHERE m.prestationId = :prestationId AND m.supprime = '-1' ORDER BY m.dateUpload DESC")
    List<MediaPrestation> findByPrestationId(@Param("prestationId") Integer prestationId);

    @Query("SELECT m FROM MediaPrestation m WHERE m.visiteId = :visiteId AND m.naturePrestation = :nature AND m.supprime = '-1' ORDER BY m.dateUpload DESC")
    List<MediaPrestation> findByVisiteAndNature(@Param("visiteId") String visiteId, @Param("nature") String nature);
}