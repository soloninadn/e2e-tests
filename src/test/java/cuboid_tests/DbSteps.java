package cuboid_tests;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static cuboid_tests.EnvConfig.*;
import static org.junit.jupiter.api.Assertions.fail;

@UtilityClass
@Slf4j
public class DbSteps {
    public static final String PAYMENTS_FILTER =
        "accountexecutive = 'user for autotests' and createdtime > '2021-10-01'";
    public static final String DELETE_PAYMENTS =
        "delete from payments p where " + PAYMENTS_FILTER;
    public static final String PAYMENT_ID_QUERY =
        "select paymentid from payments p where "+PAYMENTS_FILTER;
    public static final String DELETE_NOTES =
        "delete from notes_history where payment_id in ("+PAYMENT_ID_QUERY+")";
    public static final String DELETE_APPROVAL =
        "delete from approval_info where payment_id in ("+PAYMENT_ID_QUERY+")";
    public static final String DELETE_WITHDRAW_INFO =
        "delete from withdraw_info where id in ("+PAYMENT_ID_QUERY+")";

    private Connection con = null;

    public void cleanDB() {
        try {
            execQuery(DELETE_NOTES);
            execQuery(DELETE_APPROVAL);
            execQuery(DELETE_WITHDRAW_INFO);
            execQuery(DELETE_PAYMENTS);
        } catch (SQLException e) {
            log.error("DB error", e);
            fail("failed to clean DB");;
        } finally {
            closeConnection();
        }
    }

    private void execQuery(String query) throws SQLException {
        Statement stmt = getConnection().createStatement();
        stmt.executeUpdate(query);
    }

    private void closeConnection() {
        try {
            if (null != con) {
                con.close();
            }
            con = null;
        } catch (SQLException e) {
            log.error("DB error", e);
            fail("failed to close DB connection");;
        }
    }

    private Connection getConnection() throws SQLException {
        if (null != con) {
            return con;
        }
        log.info("Connect to DB: URL={}", DB_URL);
        con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        return con;
    }
}
