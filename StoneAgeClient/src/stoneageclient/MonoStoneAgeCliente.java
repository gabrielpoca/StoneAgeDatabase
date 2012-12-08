package stoneageclient;

import stoneageserver.DatabaseInterface;

/**
 * Created with IntelliJ IDEA.
 * User: rafaelremondes
 * Date: 12/12/08
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class MonoStoneAgeCliente {

    public static void main(String[] args){
    try {

        DataInputStream ois = new DataInputStream(System.in);
        Registry registry = LocateRegistry.getRegistry(Integer.valueOf(1098));
        DatabaseInterface db = (DatabaseInterface) registry.lookup("/localhost:1098/connect");
        byte[] buffer = new byte[1024];
        ois.read(buffer);
        while (buffer != null) {
            String[] command = null;
            command = new String(buffer).split(" ");
            String argumments = "";
            String cmd = "";
            String key = "";
            int x = 0;
            for (String s : command) {
                if (x == 0) {
                    s = s.replaceAll("\n", "");
                    cmd = s;
                }
                if (x == 1) {
                    s = s.replaceAll("\n", "");
                    key = s;
                }
                if (x > 1) {
                    s = s.replaceAll("\n", "");
                    s = s.concat(" ");
                    argumments = argumments.concat(s);
                }
                x++;
            }
            x = 0;
            if (cmd.equals("put") == true) {
                if (argumments.equals("")) {
                    System.out.println("illegal statment");
                } else {
                    byte[] argz = argumments.getBytes();
                    db.put(key, argz);
                }
            }
            if (cmd.equals("get") == true) {
                byte[] argz = db.get(key);
                if (argz != null) {
                    System.out.println("What i get:" + new String(argz));
                } else {
                    System.out.println("Source not found at all lol");
                }
            }
            if (cmd.contains("getAll") == true) {
                Map<String, byte[]> pairs = new HashMap<String, byte[]>();
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
                pairs = db.getAll(sets);
                for (String s : pairs.keySet()) {
                    if(s!=null){
                        byte[] carlos = pairs.get(s);
                        if(carlos!=null){
                            String value = new String(carlos);
                            System.out.println("Key: " + s + " Value: " + value);
                        }
                        else{
                            System.out.println("Sou nulo");
                        }
                    }
                }


            }
            if (cmd.contains("putAll")) {
                Map<String, byte[]> pairs = new HashMap<String, byte[]>();
                int i = 0;
                do {
                    buffer = null;
                    buffer = new byte[200];
                    ois.read(buffer);
                    command = new String(buffer).split(" ");
                    key = command[0];
                    if (key.equals("exit") != true) {
                        command[command.length - 1] = command[command.length - 1].replaceAll("\n", "");
                        argumments = "";
                        for (i = 1; i < command.length; i++) {
                            argumments = argumments.concat(" " + command[i]);
                        }
                        pairs.put(key, argumments.getBytes());
                    }
                } while (command[0].equals("exit") != true);
                db.putAll(pairs);
            }
            buffer = null;
            buffer = new byte[200];
            ois.read(buffer);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

}
}

