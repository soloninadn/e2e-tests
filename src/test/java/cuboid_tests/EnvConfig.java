package cuboid_tests;

import java.time.Duration;

public class EnvConfig {
    public final static String PAYAPI_URL = "https://www.i.ua/";
    public final static String PAYAPI_USERNAME = "dimas@autotests.com";
    public final static String PAYAPI_PASSWORD = "Secret123";

    public final static String DB_URL = "jdbc:postgresql://3.249.201.27:5432/payapidb";
    public final static String DB_USERNAME = "payapi";
    public final static String DB_PASSWORD = "payapi123";

    public final static int DEFAULT_TIMEOUT_MSEC = 30_000;
    public final static int TIMEOUT_MSEC = 5_000;
    public final static int SHORT_TIMEOUT_MSEC = 1_000;
    public final static Duration MAX_TIMEOUT_SEC = Duration.ofSeconds(60);
    public final static Duration TIMEOUT_SEC = Duration.ofMillis(TIMEOUT_MSEC);

    public final static String MAM = "1000000502";
    public final static String PAMM = "6025595";
    public final static String REGULAR = "2001675";
    public final static String WALLET = "222021250";
}
