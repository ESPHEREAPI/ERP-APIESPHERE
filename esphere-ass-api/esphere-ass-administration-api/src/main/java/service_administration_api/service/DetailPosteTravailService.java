/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.beans.factory.annotation.Autowired;

import service_administration_api.entite.DetailPosteTravail;
import service_administration_api.entite.DetailPosteTravailId;
import service_administration_api.repository.DetailPosteTravailRepository;
import service_administration_api.repository.DetailPosteTravailSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 *
 * @author USER01
 */
public class DetailPosteTravailService {
    
    @Autowired
    private DetailPosteTravailRepository repository;
    
    @Autowired
    private DetailPosteTravailSpecificationRepository specRepository;
    
    // === MÉTHODES DE BASE ===
    
    public DetailPosteTravail findById(String nomUtil, Integer codeInte, Integer codeBran, Integer codecate) {
        DetailPosteTravailId id = new DetailPosteTravailId(nomUtil, codeInte, codeBran, codecate);
        return repository.findById(id).orElse(null);
    }
    
    public DetailPosteTravail save(DetailPosteTravail detail) {
        return repository.save(detail);
    }
    
    public void delete(String nomUtil, Integer codeInte, Integer codeBran, Integer codecate) {
        DetailPosteTravailId id = new DetailPosteTravailId(nomUtil, codeInte, codeBran, codecate);
        repository.deleteById(id);
    }
    
    public List<DetailPosteTravail> findAll() {
        return repository.findAll();
    }
    
    // === MÉTHODES MÉTIER ===
    
    public List<DetailPosteTravail> findByUser(String nomUtil) {
        return repository.findByIdNomUtil(nomUtil);
    }
    
    public List<DetailPosteTravail> findValidatedByUser(String nomUtil) {
        return repository.findByIdNomUtilAndValiAnAndValiAv(nomUtil, "O", "O");
    }
    
    public List<DetailPosteTravail> findByBranche(Integer codeBran) {
        return repository.findByIdCodeBran(codeBran);
    }
    
    // === RECHERCHE DYNAMIQUE AVEC SPECIFICATIONS ===
    
    public Page<DetailPosteTravail> findWithFilters(String nomUtil, Integer codeBran, 
                                                   Integer codecate, String valiAn, 
                                                   Pageable pageable) {
        
        Specification<DetailPosteTravail> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (nomUtil != null && !nomUtil.isEmpty()) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("id").get("nomUtil"), nomUtil));
            }
            
            if (codeBran != null) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("id").get("codeBran"), codeBran));
            }
            
            if (codecate != null) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("id").get("codecate"), codecate));
            }
            
            if (valiAn != null && !valiAn.isEmpty()) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("valiAn"), valiAn));
            }
            
            return criteriaBuilder.and((jakarta.persistence.criteria.Predicate[]) predicates.toArray(new Predicate[0]));
        };
        
        return specRepository.findAll(spec, pageable);
    }
    
    // === MÉTHODES STATISTIQUES ===
    
    public long countByUser(String nomUtil) {
        return repository.countByIdNomUtil(nomUtil);
    }
    
    public long countValidated() {
        return repository.countByValiAnAndValiAv("O", "O");
    }
    
    // === MÉTHODES BATCH ===
    
    @Transactional
    public void validateAllByUser(String nomUtil) {
        List<DetailPosteTravail> details = repository.findByIdNomUtil(nomUtil);
        details.forEach(detail -> {
            detail.setValiAn("O");
            detail.setValiAv("O");
        });
        repository.saveAll(details);
    }
    
    @Transactional
    public void deleteAllByUser(String nomUtil) {
        repository.deleteByIdNomUtil(nomUtil);
    }

}
