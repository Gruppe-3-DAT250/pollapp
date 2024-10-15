package gruppe3.pollapp;

import org.springframework.boot.SpringApplication;

public class TestPollappApplication {

    public static void main(String[] args) {
        SpringApplication.from(PollappApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
