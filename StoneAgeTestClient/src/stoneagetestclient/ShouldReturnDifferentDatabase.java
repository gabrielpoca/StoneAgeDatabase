package stoneagetestclient;

import com.github.javafaker.Faker;
import database.DatabaseInterface;

public class ShouldReturnDifferentDatabase extends Test {
    private int port;

    public ShouldReturnDifferentDatabase(Faker faker, int port) {
        super(faker);
        this.port = port;
    }

    public void run() {
        try {
            requestDatabaseFromRMI(port);

            DatabaseInterface database = requestDatabaseFromRMI(port);
            database.put(faker.firstName(), faker.sentence(50).getBytes());

            DatabaseInterface database_two = requestDatabaseFromRMI(port);

            if(database.getPort() != database_two.getPort())
                success();
            else
                fail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
