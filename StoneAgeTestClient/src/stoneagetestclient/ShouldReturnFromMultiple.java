package stoneagetestclient;


import com.github.javafaker.Faker;
import database.DatabaseInterface;
import state.StateClientInterface;
import state.StateInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShouldReturnFromMultiple extends Test {

    private int port_one;
    private int port_two;
    private int port_three;

    public ShouldReturnFromMultiple(Faker faker, int port_one, int port_two, int port_three) {
        super(faker);
        this.port_one = port_one;
        this.port_two = port_two;
        this.port_three = port_three;
    }

    public void run() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            String key_one = faker.firstName();
            String info_one = faker.sentence(200);

            String key_two = faker.firstName();
            String info_two = faker.sentence(160);

            String key_three = faker.firstName();
            String info_three = faker.sentence(150);


            DatabaseInterface database_one = getDatabaseFromRMI(port_one);
            database_one.resetDatabase();
            database_one.put(key_one, info_one.getBytes());

            DatabaseInterface database_two = getDatabaseFromRMI(port_two);
            database_two.resetDatabase();
            database_two.put(key_two, info_two.getBytes());

            DatabaseInterface database_three = getDatabaseFromRMI(port_three);
            database_three.resetDatabase();
            database_three.put(key_three, info_three.getBytes());

            DatabaseInterface test_database = requestDatabaseFromRMI(port_one);

            ArrayList<String> keys = new ArrayList<String>();
            keys.add(key_one);
            keys.add(key_two);
            keys.add(key_three);
            Map<String, byte[]> res = test_database.getAll(keys);

            boolean success = true;
            if(!res.get(key_one).equals(info_one))
                    success = false;
            if(!res.get(key_two).equals(info_two))
                success = false;
            if(!res.get(key_three).equals(info_three))
                success = false;

            if(success)
                success();
            else fail();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
