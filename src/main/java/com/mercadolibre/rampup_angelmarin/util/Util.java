package com.mercadolibre.rampup_angelmarin.util;

import java.util.UUID;

public class Util {

    public static boolean onlyLetters(String var){
        if(var.matches("[a-zA-Z]")){
            return true;
        }
        return false;
    }

    public static boolean notNull(String var){
        if(var != null && !var.isEmpty() ){
            return true;
        }
        return false;
    }

    public static String validateFruit(String name, String owner) throws Exception {
        String msg = null;

        boolean res = Util.onlyLetters(name);
        if(res){
            throw new Exception("El nombre debe ser alfabetico");
        }
        else{
            if(!Util.notNull(owner)){
                throw new Exception("El owner no debe estar vacío");
            }
        }
        return msg;
    }

    public static String generateUUID(){
        return  UUID.randomUUID().toString();

    }
}
