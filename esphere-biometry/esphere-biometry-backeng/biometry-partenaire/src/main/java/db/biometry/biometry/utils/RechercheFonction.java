/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.utils;

/**
 *
 * @author USER01
 */
public class RechercheFonction {
    // retourner code risque
    //1184_1017-2130000077
    public static Integer codeRisque(String code_assure_principal){
        String[] data = code_assure_principal.split("\\_");
        int code =Integer.parseInt(data[0]);
        return code;
    }
    //601_1017-2130000100_32
      public static Integer codeMembre(String code_beneficiaire){
        String[] data = code_beneficiaire.split("\\_");
        int code =Integer.parseInt(data[2]);
        return code;
    }
      
//      /601_1017-2130000100_32
      public static Integer codeAgence(String code_assure_principal){
        String[] data = code_assure_principal.split("\\_");
        String retourd=data[1];
        data=retourd.split("-");
        int code =Integer.parseInt(data[0]);
        return code;
    }
        public static Integer codepolice(String code_assure_principal){
        String[] data = code_assure_principal.split("\\_");
        String retourd=data[1];
        data=retourd.split("-");
        int code =Integer.parseInt(data[1]);
        return code;
    }
      
}
