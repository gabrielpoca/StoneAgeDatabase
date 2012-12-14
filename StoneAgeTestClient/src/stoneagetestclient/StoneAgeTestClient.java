package stoneagetestclient;

import com.github.javafaker.Faker;


public class StoneAgeTestClient extends Thread {

    public StoneAgeTestClient() {

    }

    public void run() {
        Faker faker = new Faker();

        ShouldPutAndGet test_one = new ShouldPutAndGet(faker, 1099);
        test_one.run();

        ShouldReturnDifferentDatabase test_two = new ShouldReturnDifferentDatabase(faker, 1099);
        test_two.run();

        ShouldReturnFromMultiple test_three = new ShouldReturnFromMultiple(faker, 1099, 1098, 1097);
        test_three.run();
    }

    public static void main(String args[]) {
        Thread t = new Thread(new StoneAgeTestClient());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
