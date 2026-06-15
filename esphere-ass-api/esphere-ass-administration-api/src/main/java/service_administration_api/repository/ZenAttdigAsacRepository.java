package service_administration_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service_administration_api.entite.ZenAttdigAsac;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ZenAttdigAsacRepository extends JpaRepository<ZenAttdigAsac, String> {

    // 🔍 Recherche simple
    //List<ZenAttdigAsac> findByNumeroPolice(String numeroPolice);
    Optional<ZenAttdigAsac> findByImmatriculation(String immatriculation);

    // 🔍 Recherche par assuré
    List<ZenAttdigAsac> findByNomAssureContainingIgnoreCase(String nom);

    List<ZenAttdigAsac> findByTelAssure(String telephone);

    // 🔍 Recherche par souscripteur
    List<ZenAttdigAsac> findByNomSouscripteurContainingIgnoreCase(String nom);

    // 🔍 Recherche véhicule
    List<ZenAttdigAsac> findByMarque(String marque);

    List<ZenAttdigAsac> findByCategorie(String categorie);

    List<ZenAttdigAsac> findByUsage(String usage);

    // 🔍 Recherche par période (contrat)
    List<ZenAttdigAsac> findByDateEffetBetween(LocalDate debut, LocalDate fin);

    List<ZenAttdigAsac> findByDateEcheanceBefore(LocalDate date);

    List<ZenAttdigAsac> findByDateEcheanceAfter(LocalDate date);

    // 🔍 Recherche combinée
    List<ZenAttdigAsac> findByNomAssureContainingIgnoreCaseAndUsage(String nom, String usage);

    List<ZenAttdigAsac> findByMarqueAndCategorie(String marque, String categorie);

    // 🔍 Conducteur
    List<ZenAttdigAsac> findByNumeroPermis(String numeroPermis);

    // 🔍 Zone / agence
    List<ZenAttdigAsac> findByZoneCirculation(String zone);

    List<ZenAttdigAsac> findByOfficeCode(Integer officeCode);

    // 🔍 Comptage
    long countByUsage(String usage);

    long countByDateEffetBetween(LocalDate debut, LocalDate fin);

    // Remplacer cette méthode dérivée par une @Query native
    @Query(value = """
        SELECT /*+ FIRST_ROWS(1) NO_MERGE */
            q.codeinte                                          AS office_code,
            'ASAC_Zenithe'                                      AS organization_code,
            'Cima'                                              AS certificate_type,
            'auto'                                              AS couleur_d_attestation_a_editer,
            hr.numechas                                         AS numero_de_chassis_du_vehicule,
            hr.numeimma                                         AS immatriculation_du_vehicule,
            hr.modele                                           AS modele_du_vehicule,
            hr.modele                                           AS type_du_vehicule,
            hr.marque                                           AS marque_du_vehicule,
            hr.nombplac                                         AS nombre_de_place_du_vehicule,
            hr.puisvehi                                         AS puissance_fiscale,
            hr.codezone                                         AS zone_de_circulation,
            hr.date_mec                                         AS date_mise_en_circulation,
            hr.conducteur                                       AS nom_prenom_conducteur,
            hr.datenais_cond                                    AS date_de_naissance_conducteur,
            hr.permis                                           AS numero_permis,
            hr.codepermis                                       AS categorie_permis,
            hr.datdelper                                        AS date_de_delivrance_permis,
            DECODE(hr.typemote,'E','Essence','D','Diezel','Autre') AS energie_du_vehicule,
            ga.libgenau                                         AS genre_du_vehicule,
            ua.libusaau                                         AS usage_du_vehicule,
            DECODE(q.codecate,
                1010,'01',1020,'02',1030,'03',1040,'04',1050,'05',
                1060,'06',1070,'07',1080,'08',1090,'09',1100,'10'
            )                                                   AS categorie_du_vehicule,
            a.genrassu                                          AS type_de_souscripteur,
            a.teleassu                                          AS numero_de_tel_du_souscripteur,
            a.adreassu                                          AS boite_postale_du_souscripteur,
            a.mailres1                                          AS adresse_email_du_souscripteur,
                     a.codeassu AS CODE_ASSURE,
                             a.codeprof AS PROFESSION_DE_L_ASSURE,
                             a.codevill AS VILLE_DE_L_ASSURE,
            a.raissoci || ' ' || a.prenassu                     AS nom_du_souscripteur,
            a.teleassu                                          AS telephone_mobile_de_l_assure,
            a.adreassu                                          AS boite_postale_de_l_assure,
            a.mailres1                                          AS adresse_email_de_l_assure,
            NVL(hr.nom_agen, a.raissoci||' '||a.prenassu)       AS nom_de_l_assure,
            a.datecrea                                          AS date_de_naissance_de_l_assure,
            a.regicomm                                          AS taxpayer_number,
            r.poidvehi                                          AS vehicle_gross_weight,
            q.dateeffe                                          AS date_d_effet_du_contrat,
            q.dateeche                                          AS date_d_echeance_du_contrat,
            q.codeinte || '-' || q.numepoli
                || DECODE(NVL(q.numeaven,0),0,'','-'||q.numeaven) AS numero_de_police,
            rc.prime_rc                                         AS prime_rc
        FROM (
            SELECT /*+ NO_MERGE */
                typemouv, codeinte, numepoli, codecate,
                coderisq, codeassu, dateeffe, dateeche, numeaven, numequit
            FROM v_ch_affaire_garantie
            WHERE datecomp BETWEEN '01/01/2026' AND SYSDATE
              AND codecate BETWEEN 1010 AND 1114
              AND codeinte || '-' || numepoli
                  || DECODE(NVL(numeaven,0),0,'','-'||numeaven) = :numeroPolice
            GROUP BY
                typemouv, numequit, codeinte, numepoli,
                codecate, coderisq, codeassu,
                dateeffe, dateeche, numeaven
        ) q
        JOIN (
            SELECT codeinte, numepoli, coderisq,
                MAX(numechas) numechas, MAX(numeimma) numeimma,
                MAX(nombplac) nombplac, MAX(typevehi) modele,
                MAX(marqvehi) marque, MAX(codgenau) codgenau,
                MAX(puisvehi) puisvehi, MAX(typemote) typemote,
                MAX(codezone) codezone, MAX(date_mec) date_mec,
                MAX(nom_agen) nom_agen, MAX(liberisq) conducteur,
                MAX(datenais) datenais_cond, MAX(numeperm) permis,
                MAX(codtyppe) codepermis, MAX(datdelpe) datdelper,
                MAX(codusaau) codusaau
            FROM hist_risque
            GROUP BY codeinte, numepoli, coderisq
        ) hr ON hr.codeinte = q.codeinte
            AND hr.numepoli = q.numepoli
            AND hr.coderisq = q.coderisq
        JOIN assure a ON a.codeassu = q.codeassu
        LEFT JOIN genre_auto ga ON ga.codgenau = hr.codgenau
        LEFT JOIN usage_auto ua ON ua.codusaau = hr.codusaau
        LEFT JOIN risque r ON r.codeinte = q.codeinte
            AND r.numepoli = q.numepoli
            AND r.coderisq = q.coderisq
        LEFT JOIN (
            SELECT codeinte, numepoli, coderisq, numeaven, typemouv,
                SUM(CASE WHEN codegara='A' THEN primnett ELSE 0 END) prime_rc
            FROM v_ch_affaire_garantie
            WHERE datecomp BETWEEN '01/01/2026' AND SYSDATE
              AND codecate BETWEEN 1010 AND 1114
            GROUP BY codeinte, numepoli, coderisq, numeaven, typemouv
        ) rc ON rc.codeinte = q.codeinte
            AND rc.numepoli = q.numepoli
            AND rc.coderisq = q.coderisq
            AND NVL(rc.numeaven,0) = NVL(q.numeaven,0)
            AND rc.typemouv = q.typemouv
        """, nativeQuery = true)
    List<ZenAttdigAsac> findByNumeroPoliceNative(
            @Param("numeroPolice") String numeroPolice
    );

    @Query(value = """
    SELECT /*+ FIRST_ROWS(1) */
        q.codeinte                                              AS office_code,
        'ASAC_Zenithe'                                          AS organization_code,
        'Cima'                                                  AS certificate_type,
        'auto'                                                  AS couleur_d_attestation_a_editer,
        hr.numechas                                             AS numero_de_chassis_du_vehicule,
        hr.numeimma                                             AS immatriculation_du_vehicule,
        hr.modele                                               AS modele_du_vehicule,
        hr.modele                                               AS type_du_vehicule,
        hr.marque                                               AS marque_du_vehicule,
        hr.nombplac                                             AS nombre_de_place_du_vehicule,
        hr.puisvehi                                             AS puissance_fiscale,
        hr.codezone                                             AS zone_de_circulation,
        hr.date_mec                                             AS date_mise_en_circulation,
        hr.conducteur                                           AS nom_prenom_conducteur,
        hr.datenais_cond                                        AS date_de_naissance_conducteur,
        hr.permis                                               AS numero_permis,
        hr.codepermis                                           AS categorie_permis,
        hr.datdelper                                            AS date_de_delivrance_permis,
        DECODE(hr.typemote,'E','Essence','D','Diezel','Autre')  AS energie_du_vehicule,
        ga.libgenau                                             AS genre_du_vehicule,
        ua.libusaau                                             AS usage_du_vehicule,
        DECODE(q.codecate,
            1010,'01',1020,'02',1030,'03',1040,'04',1050,'05',
            1060,'06',1070,'07',1080,'08',1090,'09',1100,'10')  AS categorie_du_vehicule,
        a.genrassu                                              AS type_de_souscripteur,
        a.teleassu                                              AS numero_de_tel_du_souscripteur,
        a.adreassu                                              AS boite_postale_du_souscripteur,
        a.mailres1                                              AS adresse_email_du_souscripteur,
              a.codeassu AS CODE_ASSURE,
                             a.codeprof AS PROFESSION_DE_L_ASSURE,
                             a.codevill AS VILLE_DE_L_ASSURE,       
        a.raissoci || ' ' || a.prenassu                         AS nom_du_souscripteur,
        a.teleassu                                              AS telephone_mobile_de_l_assure,
        a.adreassu                                              AS boite_postale_de_l_assure,
        a.mailres1                                              AS adresse_email_de_l_assure,
        NVL(hr.nom_agen, a.raissoci || ' ' || a.prenassu)       AS nom_de_l_assure,
        a.datecrea                                              AS date_de_naissance_de_l_assure,
        a.regicomm                                              AS taxpayer_number,
        r.poidvehi                                              AS vehicle_gross_weight,
        q.dateeffe                                              AS date_d_effet_du_contrat,
        q.dateeche                                              AS date_d_echeance_du_contrat,
        q.codeinte || '-' || q.numepoli
            || DECODE(NVL(q.numeaven,0),0,'','-'||q.numeaven)  AS numero_de_police,
        rc.prime_rc                                             AS prime_rc
    FROM (
        SELECT typemouv, codeinte, numepoli, codecate,
               coderisq, codeassu, dateeffe, dateeche, numeaven, numequit
        FROM v_ch_affaire_garantie
        WHERE datecomp BETWEEN '01/01/2026' AND SYSDATE
          AND codecate  BETWEEN 1010 AND 1114
          AND codeinte  = :codeinte
          AND numepoli  = :numepoli
          AND ((:numeaven IS NULL AND NVL(numeaven,0) = 0)
               OR numeaven = :numeaven)
        GROUP BY typemouv, numequit, codeinte, numepoli,
                 codecate, coderisq, codeassu, dateeffe, dateeche, numeaven
    ) q
    JOIN (
        SELECT codeinte, numepoli, coderisq,
               MAX(numechas) numechas, MAX(numeimma) numeimma,
               MAX(nombplac) nombplac, MAX(typevehi) modele,
               MAX(marqvehi) marque,  MAX(codgenau) codgenau,
               MAX(puisvehi) puisvehi, MAX(typemote) typemote,
               MAX(codezone) codezone, MAX(date_mec) date_mec,
               MAX(nom_agen) nom_agen, MAX(liberisq) conducteur,
               MAX(datenais) datenais_cond, MAX(numeperm) permis,
               MAX(codtyppe) codepermis, MAX(datdelpe) datdelper,
               MAX(codusaau) codusaau
        FROM hist_risque
        WHERE codeinte = :codeinte
          AND numepoli = :numepoli
        GROUP BY codeinte, numepoli, coderisq
    ) hr ON hr.codeinte = q.codeinte
        AND hr.numepoli = q.numepoli
        AND hr.coderisq = q.coderisq
    JOIN assure a ON a.codeassu = q.codeassu
    LEFT JOIN genre_auto ga ON ga.codgenau = hr.codgenau
    LEFT JOIN usage_auto ua ON ua.codusaau = hr.codusaau
    LEFT JOIN risque r ON r.codeinte = q.codeinte
        AND r.numepoli = q.numepoli
        AND r.coderisq = q.coderisq
    LEFT JOIN (
        SELECT codeinte, numepoli, coderisq, numeaven, typemouv,
               SUM(CASE WHEN codegara='A' THEN primnett ELSE 0 END) prime_rc
        FROM v_ch_affaire_garantie
        WHERE codeinte  = :codeinte
          AND numepoli  = :numepoli
          AND datecomp BETWEEN '01/01/2026' AND SYSDATE
          AND codecate  BETWEEN 1010 AND 1114
        GROUP BY codeinte, numepoli, coderisq, numeaven, typemouv
    ) rc ON rc.codeinte = q.codeinte
        AND rc.numepoli = q.numepoli
        AND rc.coderisq = q.coderisq
        AND NVL(rc.numeaven,0) = NVL(q.numeaven,0)
        AND rc.typemouv = q.typemouv
    """, nativeQuery = true)
    List<ZenAttdigAsac> findByPoliceDecompose(
            @Param("codeinte") Long codeinte, // ✅ NUMBER → Long
            @Param("numepoli") Long numepoli, // ✅ NUMBER → Long
            @Param("numeaven") Integer numeaven // ✅ NUMBER → Integer (nullable)
    );
}
