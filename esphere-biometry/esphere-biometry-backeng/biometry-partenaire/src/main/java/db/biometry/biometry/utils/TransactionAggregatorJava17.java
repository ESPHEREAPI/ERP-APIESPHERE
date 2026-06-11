/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
////package db.biometry.biometry.utils;
////
////import db.biometry.biometry.dtos.StatistiquesParPeriodeDTO;
////import java.util.*;
////import java.time.LocalDate;
////import java.util.stream.Collectors;
////import java.util.stream.Stream;
////
////
////
/////**
//// *
//// * @author USER01
//// */
////public class TransactionAggregatorJava17 {
////    /**
////     * Méthode principale pour agréger les transactions par date
////     * Version Java 17 avec Stream API
////     */
////    public static List<StatistiquesParPeriodeDTO> agregerParDate(
////            List<StatistiquesParPeriodeDTO> consultations, 
////            List<StatistiquesParPeriodeDTO> examens) {
////        
////        // Fusionner les deux listes
////        List<StatistiquesParPeriodeDTO> toutesTransactions = Stream
////                .concat(consultations.stream(), examens.stream())
////                .collect(Collectors.toList());
////        
////        // Grouper par date et sommer les montants
//////        Map<LocalDate, Double> agregationParDate = toutesTransactions.stream()
//////                .collect(Collectors.groupingBy(
//////                        StatistiquesParPeriodeDTO::getDate(),
//////                        Collectors.summingDouble(StatistiquesParPeriodeDTO::getTotalDepenses().doubleValue()),
//////                         Collectors.summingDouble(StatistiquesParPeriodeDTO::getTotalRembourse().doubleValue())
//////                        
//////                ));
//////        
//////        // Convertir en liste de PeriodeAgregee et trier par date
//////        return agregationParDate.entrySet().stream()
//////                .map(entry -> new PeriodeAgregee(entry.getKey(), entry.getValue()))
//////                .sorted()
//////                .collect(Collectors.toList());
////    }
////}
