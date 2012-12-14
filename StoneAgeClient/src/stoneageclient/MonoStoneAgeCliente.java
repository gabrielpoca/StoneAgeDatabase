package stoneageclient;

import database.DatabaseInterface;
import state.StateClientInterface;
import stoneageserver.DatabaseInterface;

/**
 * Created with IntelliJ IDEA.
 * User: rafaelremondes
 * Date: 12/12/08
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class MonoStoneAgeCliente  implements Runnable{

       public MonoStoneAgeCliente(){}

        public void run(){
            try{
            boolean flag = false;
            DataInputStream ois = new DataInputStream(System.in);
            DatabaseInterface db = getDatabaseInterface(Integer.parseInt(args[0]));
            Thread t1;
            byte[] buffer = new byte[200];
            ois.read(buffer);
            while (buffer != null) {
                String s = new String(buffer);
                String[] command = s.split(" ");
                if(command[0].contains("putAll")){
                    flag = true;
                    Map<String, String> pairs = new HashMap<String,String>();
                    int i = 0;
                    do {
                        buffer = null;
                        buffer = new byte[200];
                        ois.read(buffer);
                        command = new String(buffer).split(" ");
                        String key = command[0];
                        if (key.equals("exit") != true || key.equals("")!= true) {
                            command[command.length - 1] = command[command.length - 1].replaceAll("\n", "");
                            String argumments = "";
                            for (i = 1; i < command.length; i++) {
                                argumments = argumments.concat(" " + command[i]);
                            }
                            pairs.put(key, argumments);
                        }
                    } while (command[0].equals("exit") != true);
                    t1 = new Thread(new Parser(db,"putAll",pairs,null));
                    t1.start();
                }
                if(command[0].contains("getAll")){
                    flag = true;
                    Collection<String> sets = new ArrayList<String>();
                    String set;
                    do{
                        buffer = null;
                        buffer = new byte[200];
                        ois.read(buffer);
                        set = new String(buffer);
                        set = set.replaceAll("\n", "");
                        command = set.split(" ");
                        if(set.contains("exit")!=true){
                            sets.add(command[0]);
                        }
                    }
                    while(set.contains("exit")!=true);
                    t1 = new Thread(new Parser(db,"getAll",null,sets));
                    t1.start();
                }
                if(command[0].contains("get") || command[0].contains("put")){
                    flag = true;
                    t1 = new Thread(new Parser(db,s,null,null));
                    t1.start();
                }
                if(flag!=true){
                    print("Illegal statmment");
                }
                flag = false;
                buffer = new byte[200];
                ois.read(buffer);

            }
        }
    catch(Exception e){  }
}

    public void print(String s){
        System.out.println(s);
    }

    private  DatabaseInterface getDatabaseInterface(int port){
        Registry registry = LocateRegistry.getRegistry(Integer.valueOf(1099));
        StateClientInterface state = (StateClientInterface) registry.lookup("/localhost:1099/connect");
        DatabaseInterface database = state.requestDatabase();
        return database;
    }

    public static void main(String[] args){
    try {
         Thread t1 = new Thread(new MonoStoneAgeCliente());
         t1.start();
         t1.join();

    } catch (Exception e) {
        e.printStackTrace();
    }

}
}

