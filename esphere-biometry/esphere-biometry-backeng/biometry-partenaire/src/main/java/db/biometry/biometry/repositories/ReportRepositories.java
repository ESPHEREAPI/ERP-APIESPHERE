/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.entite.Report;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author USER01
 */
public interface ReportRepositories extends JpaRepository<Report, Long>{
    
    
}
