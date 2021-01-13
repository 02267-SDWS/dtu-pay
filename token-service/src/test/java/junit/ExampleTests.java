package junit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.TokenService;
import services.interfaces.ITokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTests {

    private ITokenService service;

    @BeforeEach
    public void setUp() {
        service = new TokenService();
        System.out.println("Setting up...");
    }

    @AfterEach
    public void tearDown() {
        // Put teardown code here if needed.
        System.out.println("Tearing down...");
    }

    @Test
    public void testHealth() {
        String expected = "I am healthy and ready to work!";

        // The third argument to assertEquals is a message displayed when the
        // test fails. This is useful to rapidly understand what the test expects.
        assertEquals(expected, "", "The two strings should be equal!");
    }

    @Test
    public void exampleRead() {
        String expected = "Example obj";
        // The third argument to assertEquals is a message displayed when the
        // test fails. This is useful to rapidly understand what the test expects.
        assertEquals(expected, "", "The two strings should be equal!");
    }

}
