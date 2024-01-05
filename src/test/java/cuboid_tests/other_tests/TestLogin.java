package cuboid_tests.other_tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static cuboid_tests.PayapiSteps.createTransaction;
import static cuboid_tests.PayapiSteps.login;

@Tag("smoke-tests")
public class TestLogin {

    @Test
    void tryToLogin(){
        // log in cuboid and check that displayed Transactions tab
        login();
        //createTransaction();
    }
}
