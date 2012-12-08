package stoneageclient;

import stoneageserver.DatabaseInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;


public class Parser implements Runnable{

    String s;
    DatabaseInterface db;
    Map<String, byte[]> pairs;
    Collection<String> sets;

    public Parser(DatabaseInterface db,String order, Map<String, byte[] > pairs, Collection<String> sets) throws NotBoundException, MalformedURLException, RemoteException{
        this.db = db;
        if(pairs!=null){
            this.pairs = pairs;
        }
        if(sets!=null){
            this.sets = sets;
        }
        s = order;
    }

    @Override
    public void run(){
        try{
            String[] command = null;
            command = this.s.split(" ");
            String argumments = "";
            String cmd = "";
            String key = "";
            int x = 0;
            for(String s : command){
                if(x == 0){
                    s = s.replaceAll("\n","");
                    cmd = s;
                }
                if(x == 1) {
                    s = s.replaceAll("\n","");
                    key = s;
                }
                if( x>1) {
                    s = s.replaceAll("\n","");
                    s = s.concat(" ");
                    argumments = argumments.concat(s);
                }
                x++;
            }
            x = 0;
            if(cmd.equals("put") == true){
                if(argumments.equals("")){
                    System.out.println("illegal statment");
                }
                else{
                    byte[] argz = argumments.getBytes();
                    db.put(key, argz);
                }
            }
            if(cmd.equals("get") == true){
                byte[] argz = db.get(key);
                if(argz!=null){
                    System.out.println("What i get:"+ new String(argz));
                }
                else{
                    System.out.println("Source not found at all lol");
                }
            }
            if(cmd.equals("getAll") == true){
                Map<String,byte[]> pair = new HashMap<String, byte[]>();
                pair = db.getAll(sets);
                for(String s : pair.keySet()){
                    String value = new String(pair.get(s));
                    System.out.println("Key: "+s+" Value: "+value);
                }

            }
            if(cmd.equals("putAll") == true){
                db.putAll(pairs);
            }
        }
        catch(Exception e){}

    }
}
