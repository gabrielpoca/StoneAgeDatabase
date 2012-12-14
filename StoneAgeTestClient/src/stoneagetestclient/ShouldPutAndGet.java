package stoneagetestclient;


import com.github.javafaker.Faker;
import database.DatabaseInterface;

/**
 * When putting something in a database it should be returned when calling get with the same key.
 */

public class ShouldPutAndGet extends Test {

    private int port;

    public ShouldPutAndGet(Faker faker, int port) {
        super(faker);
        this.port = port;
    }

    public void run() {
        try {
            DatabaseInterface database = requestDatabaseFromRMI(port);
            String key = faker.firstName();
            String expected = faker.sentence(40);

            database.put(key, expected.getBytes());

            Thread.sleep(500);

            String returned = (new String(database.get(key))).trim();
            test(expected, returned, "Basic");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void test(String expected, String returned, String test_name) {
        if(returned.equals(expected))
            success();
        else
            fail();
    }
}
