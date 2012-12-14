package stoneagetestclient;

import com.github.javafaker.Faker;
import database.DatabaseInterface;
import org.slf4j.LoggerFactory;
import state.StateClientInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.ConsoleHandler;
import org.slf4j.Logger;

import database.DatabaseInterface;
import state.StateInterface;
import sun.jvm.hotspot.debugger.win32.coff.COFFException;



/**
 * This class is a test template. It contains code useful for every test.
 */

public class Test {

    protected Faker faker;

    public Test(Faker faker) {
        this.faker = faker;
    }

    protected void success() {
        System.out.println(this.getClass().getName()+" finished with success!");
    }

    protected void fail() {
        System.out.println(this.getClass().getName() + " failed!");
    }

    protected DatabaseInterface requestDatabaseFromRMI(int port) {
        DatabaseInterface database = null;
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
            StateClientInterface state = (StateClientInterface) registry.lookup("/localhost:"+1099+"/connect");
            database = state.requestDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return database;
    }

    /**
     * This method returns the handler local database.
     * @param port RMI port to connect to.
     * @return A DatabaseInterface object.
     */
    protected DatabaseInterface getDatabaseFromRMI(int port) {
        DatabaseInterface database = null;
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
            StateInterface state = (StateInterface) registry.lookup("/localhost:"+port+"/connect");
            database = state.getDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return database;
    }

    protected void resetDatabaseFromRMI(int port) {
        DatabaseInterface database = null;
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
            StateInterface state = (StateInterface) registry.lookup("/localhost:"+port+"/connect");
            database = state.getDatabase();
            database.resetDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
