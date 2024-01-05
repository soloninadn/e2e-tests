package cuboid_tests.hvt_tab;

import cuboid_tests.PayapiSteps;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static cuboid_tests.EnvConfig.*;
import static cuboid_tests.HvtSteps.setBankFields;
import static cuboid_tests.PayapiSteps.createAndApplyTransactionsButton;

@Tag("balance-tests")
public class DepositAccountsTests {

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void makeDepositCreateAndApply(String accountNumber) {
        String amountStr = Integer.toString(PayapiSteps.MAX_AMOUNT * 30);

        //// log in and make a deposit
        PayapiSteps.login();
        PayapiSteps.createTransaction();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byId("executedAmount")).shouldBe(visible);
        $(byCssSelector("div.form-group")).click();
        sleep(TIMEOUT_MSEC);
        $(byId("executedAmount")).setValue(amountStr);
        setBankFields();
        $(byClassName("btn-success")).click();
        createAndApplyTransactionsButton();
        $(byId("confirmAndApplyButton")).click();

        // check that last transaction in list has needed transaction sum
        $(byId("transactionDepositSum")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("transactionDepositSum")).shouldHave(text(amountStr));
    }
}
