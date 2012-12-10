package stoneagetestclient;

import com.github.javafaker.Faker;
import database.DatabaseFileException;
import database.DatabaseInterface;
import state.StateClientInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



public class StoneAgeTestClient extends Thread {

    Faker faker;
    Logger logger;

    public StoneAgeTestClient() {
        faker = new Faker();
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        logger = Logger.getLogger(StoneAgeTestClient.class.getName());
        logger.addHandler(ch);
        logger.setLevel(Level.ALL);
    }

    public void run() {

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

        Basic basic = new Basic(1099);
        basic.run();

        BasicTwo basic_two = new BasicTwo(1099, 1098);
        basic_two.run();
    }


    class BasicTwo extends Basic {

        private int port_two;

        public BasicTwo(int port, int port_two) {
            super(port);
            this.port_two = port_two;
        }

        public void run() {
            try {
                DatabaseInterface database_1 = getDatabaseFromRMI(super.port);
                DatabaseInterface database_2 = getDatabaseFromRMI(port_two);

                String key = faker.firstName();
                String msg = faker.sentence(50);
                database_1.put(key, msg.getBytes());

                String res = (new String(database_2.get(key))).trim();

                test(msg, res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Basic {

        private String key;
        private String msg;

        private int port;

        public Basic(int port) {
            this.port = port;
        }

        public void run() {
            try {
                DatabaseInterface database = getDatabaseFromRMI(port);
                key = faker.firstName();
                msg = faker.sentence(40);

                database.put(key, msg.getBytes());
                Thread.sleep(1000);
                String res = (new String(database.get(key))).trim();
                test(msg, res);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        protected void test(String msg, String res) {
            if(res.equals(msg))
                logger.fine("Test Basic exited with success!");
            else
                logger.warning("Test Basic failed!\n\tSent: "+msg+"\n\tReceived: "+res);

        }
    }

    private DatabaseInterface getDatabaseFromRMI(int port) {
        DatabaseInterface database = null;
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(1099));
            StateClientInterface state = (StateClientInterface) registry.lookup("/localhost:1099/connect");
            database = state.requestDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return database;
    }

    public static void main(String args[]) {
        Thread t = new Thread(new StoneAgeTestClient());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
