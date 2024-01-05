package cuboid_tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.*;
import static cuboid_tests.EnvConfig.*;


public class PayapiSteps {
    public static final int MAX_AMOUNT = 29;

    public static void login() {
//        SalesforceSteps.checkAccounts();
//        DbSteps.cleanDB();
        Configuration.timeout = EnvConfig.DEFAULT_TIMEOUT_MSEC;
        // log in admin
        open(PAYAPI_URL);
//        element(byId("username")).setValue(PAYAPI_USERNAME);
//        element(byId("password")).setValue(PAYAPI_PASSWORD);
//        element(byId("loginButton")).click();
    }

    public static void createTransaction() {
        Selenide.sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
        $(byId("highValueLink")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC).click();
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
        $(byId("createTransactionButton")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC).click();
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
    }

    public static void clickUpdateButton(){
        $(byId("showButton")).shouldBe(visible).click();
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
    }

    public static void setDateRange(String dateFrom, String dateTo){
        $(byId("dateFromInput")).clear();
        $(byId("dateFromInput")).setValue(dateFrom).pressEnter();
        $(byId("dateToInput")).clear();
        $(byId("dateToInput")).setValue(dateTo).pressEnter();
    }

    public static void setDateFrom(String dateFrom){
        $(byId("dateFromInput")).clear();
        $(byId("dateFromInput")).setValue(dateFrom).pressEnter();
    }

    public static void setDateTo(String dateTo){
        $(byId("dateToInput")).clear();
        $(byId("dateToInput")).setValue(dateTo).pressEnter();
    }

    public static String getRandomAmount() {
        return "16.37";
    }

    public static void clickTransactionsTab() {
        Selenide.sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
        $(byId("transactionsLink")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC);
        $(byId("transactionsLink")).click();
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
    }

    public static void checkTransaction(String accountNumber, String randomAmountStr, String status) {
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldHave(text(accountNumber)).
                shouldHave(text(randomAmountStr)).shouldHave(text(status));
    }

    public static void createTransactionsButton() {
        Selenide.sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
        $(byId("createTransactionButton")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC).click();
        Selenide.sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
    }

    public static void createAndApplyTransactionsButton() {
        Selenide.sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
        $(byId("createAndApplyTransactionsButton")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC).click();
        Selenide.sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
    }
}
