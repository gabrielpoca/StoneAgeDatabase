package stoneageserver;

import java.io.File;

public class Validations {

    public static void databaseFolder(String folder) {
        File file = new File(folder);
        if(!file.exists()) {
            if(file.mkdir()) {
                log("Folder "+folder+" created!");
            } else {
                log("Failed to create folder "+folder+"! Create it and run again!");
            }
        } else {
            log("Using folder "+folder+"!");
        }
    }

    private static void log(String s) {
        System.out.println("[Validation] "+s);
    }

}
