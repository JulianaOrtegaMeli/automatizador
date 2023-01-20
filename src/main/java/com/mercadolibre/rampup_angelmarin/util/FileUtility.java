package com.mercadolibre.rampup_angelmarin.util;


import java.io.File;
import org.springframework.stereotype.Component;

@Component
public class FileUtility {


    public File[] getDirectoryFiles(String workDirectory) {
        // Especifica el directorio que deseas leer
        File folder = new File(workDirectory);
        // Obtén una lista de todos los archivos en el directorio
        if(!folder.exists())
        {
            throw new IllegalArgumentException (String.format( "no existe el directorio de trabajo %s ",workDirectory));
        }

       return folder.listFiles();

    }

}
