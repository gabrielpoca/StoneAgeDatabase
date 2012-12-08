
package stoneageclient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import stoneageserver.DatabaseInterface;
import stoneageserver.StateClientInterface;
import stoneageserver.StateInterface;


public class StoneAgeClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(1098));
            DatabaseInterface db = (DatabaseInterface) registry.lookup("/localhost:1098/connect");
           /* database.put("asds", "NOVO!".getBytes());
            System.out.print(new String(database.get("asds")));
            */

            Map<String, byte[]> pairs = new HashMap<String, byte[]>();
            Collection<String> sets = new ArrayList<String>();

            String s1 = "Sport Lisboa e Benfica";
            String s2 = "Sporting Clube de Portugal";
            String s3 = "Futebol Clube do Porto";

            pairs.put("slb", s1.getBytes());
            pairs.put("scp", s2.getBytes());
            pairs.put("fcp", s3.getBytes());

            sets.add("slb");
            sets.add("scp");
            sets.add("fcp");

            String s = "put scb Sporting Clube de Braga";


            DataBaseFactory dbFact = (DataBaseFactory) Naming.lookup("//localhost/DataBase");
            DataBase db = dbFact.make();

            Thread t1 = new Thread(new Parser(db,s,null,null));
            Thread t2 = new Thread(new Parser(db,"get scb",null, null));
            Thread t3 = new Thread(new Parser(db,"putAll",pairs, null));
            Thread t4 = new Thread(new Parser(db,"getAll",null,sets));

            t1.start();
            t1.join();
            t2.start();
            t2.join();
            t3.start();
            t3.join();
            t4.start();
            t4.join();


            Registry registry2 = LocateRegistry.getRegistry(Integer.valueOf(1099));
            DatabaseInterface database2 = (DatabaseInterface) registry2.lookup("/localhost:1099/connect");
            database2.put("asds", "NOVO!".getBytes());
            System.out.print(new String(database2.get("asds")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
