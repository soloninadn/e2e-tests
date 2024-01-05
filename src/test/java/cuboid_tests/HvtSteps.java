package cuboid_tests;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static cuboid_tests.EnvConfig.SHORT_TIMEOUT_MSEC;
import static cuboid_tests.EnvConfig.TIMEOUT_MSEC;

public class HvtSteps {

    public static void setBankFields() {
        $(byId("bankName")).should(exist).setValue("qwe");
        $(byId("bankAddress")).should(exist).setValue("qwe");
        $(byId("bankAccountName")).should(exist).setValue("qwe");
        $(byId("iban")).should(exist).setValue("qwe");
        $(byId("swift")).should(exist).setValue("qwe");
    }

    public static void selectWithdrawal() {
        sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
        $(byCssSelector("div.paymentType")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC).click();
        sleep(SHORT_TIMEOUT_MSEC);
        $(byText("Withdrawal")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC).click();
        sleep(TIMEOUT_MSEC);
    }

    public static void selectDeposit() {
        sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);
        $(byCssSelector("div.paymentType")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC).click();
        sleep(SHORT_TIMEOUT_MSEC);
        $(byText("Deposit")).shouldBe(visible, EnvConfig.MAX_TIMEOUT_SEC).click();
        sleep(TIMEOUT_MSEC);
    }
}
