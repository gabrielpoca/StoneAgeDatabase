
package stoneageclient;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

import database.DatabaseInterface;
import state.StateClientInterface;


public class StoneAgeClient {

    public static void main(String[] args) {
        try {
            DatabaseInterface database = requestDatabaseFromRMI(Integer.valueOf(args[0]));
            log("Using client on port "+database.getPort());

            String current_line = "";

            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);

            // the program exists when exit is typed
            while(!current_line.equals("exit")) {
                current_line = in.readLine();
                String[] tokens = current_line.split(" ");

                if(tokens[0].equals("put")) {
                    // if the second the third argument is a file then the file is sent
                    log("Putting key "+tokens[1]+"...");
                    File file = new File(tokens[2]);
                    if(!file.exists()) {
                        database.put(tokens[1], tokens[2].getBytes());
                    } else  {
                        database.put(tokens[1], read(file));
                    }
                } else if(tokens[0].equals("get")) {
                    // if there is a second argument that it is used do create a file with the content
                    byte[] data = database.get(tokens[1]);
                    if(args.length > 2) {
                        write(new File(tokens[2]), data);
                        log("Content in file "+tokens[2]);
                    } else {
                        String res = new String(data);
                        log("Got "+res);
                    }
                } else if(tokens[0].equals("putall")) {
                    HashMap<String, byte[]> map = new HashMap<String, byte[]>();
                    log("Type the keys and data line by line. Type done when you're done!");
                    current_line = in.readLine();
                    while(!current_line.equals("done")) {
                        String[] input_map = current_line.split(" ");
                        map.put(input_map[0], input_map[1].getBytes());
                        current_line = in.readLine();
                    }
                    log("Putting all...");
                    database.putAll(map);
                } else if(tokens[0].equals("getall")) {
                    ArrayList<String> keys = new ArrayList<String>();
                    log("Enter the keys line by line. Type done when you're done!");
                    current_line = in.readLine();
                    while(!current_line.equals("done")) {
                        String[] input_map = current_line.split(" ");
                        keys.add(input_map[0]);
                        current_line = in.readLine();
                    }
                    HashMap<String, byte[]> resMap = (HashMap<String, byte[]>) database.getAll(keys);
                    for(String key : resMap.keySet()) {
                        String res = new String(resMap.get(key));
                        log("KEY: "+key+" GOT "+res);
                    }
                } else {
                    log("Command not found...");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseInterface requestDatabaseFromRMI(int port) {
        DatabaseInterface database = null;
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
            StateClientInterface state = (StateClientInterface) registry.lookup("/localhost:"+port+"/connect");
            database = state.requestDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return database;
    }

    public static void log(String s) {
        System.out.println(s);
    }


    public static byte[] read(File file) {
        byte[] file_content = new byte[100000];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(file_content);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_content;
    }

    public static void write(File file, byte[] data) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
