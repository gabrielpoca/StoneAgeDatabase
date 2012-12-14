package stoneageclient;

import database.DatabaseFileException;
import database.DatabaseInterface;
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

    public byte[] getBytes(String s) throws FileNotFoundException, IOException{
        byte[] buffer = new byte[1024];
        File f = new File(s);
        FileInputStream fis = new FileInputStream(f);
        fis.read(buffer);
        fis.close();
        return buffer;
    }

    public void createFile(String s,byte[] buffer) throws FileNotFoundException, IOException{
        File f = new File(s);
        FileOutputStream fos = new FileOutputStream(f);
        if(buffer!=null){
            fos.write(buffer);
        }
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
                    byte[] argz = getBytes(argumments);
                    db.put(key, argz);
                }
            }
            if(cmd.equals("get") == true){
                byte[] argz = db.get(key);
                if(argz!=null){
                    System.out.println("What i get:"+ new String(argz));
                    this.createFile(key, argz);
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
                    this.createFile(key, pair.get(s));
                    System.out.println("Key: "+s+" Value: "+value);
                }

            }
            if(cmd.equals("putAll") == true){
                Map<String,byte[]> mp = new HashMap<String,byte[]>();
                for(String s : pairs.keySet()){
                    byte[] buffer = getBytes(pairs.get(s));
                    mp.put(s,buffer);
                }
                db.putAll(mp);
            }
        }
        catch(Exception e){} catch (DatabaseFileException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}