package cuboid_tests.other_tests;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static cuboid_tests.EnvConfig.REGULAR;
import static cuboid_tests.EnvConfig.TIMEOUT_SEC;
import static cuboid_tests.HvtSteps.selectWithdrawal;
import static cuboid_tests.HvtSteps.setBankFields;
import static cuboid_tests.PayapiSteps.*;

public class WithdrawalAdditionalFieldsTests {

    @Test
    void makeWithdrawalBankAccount(){
        String accountNumber = REGULAR;
        String randomAmountStr = getRandomAmount();

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).shouldBe(visible, TIMEOUT_SEC).setValue(accountNumber);
        $(byTagName("h3")).shouldBe(visible, TIMEOUT_SEC).click();
        $(byId("bankName")).shouldBe(visible);
        $(byId("executedAmount")).setValue(randomAmountStr);

        // check that displayed additional fields for Bank Account PSP
        setBankFields();
        $(byCssSelector("button.btn-success")).click();
        sleep(13000);
        createAndApplyTransactionsButton();
        $(byId("confirmAndApplyButton")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);

        // check that created transaction has success status
        clickTransactionsTab();
        checkTransaction(accountNumber, randomAmountStr, "Reconciled");
    }

    @Test
    void makeWithdrawalZenithBank(){
        String accountNumber = REGULAR;
        String randomAmountStr = getRandomAmount();

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).shouldBe(visible, TIMEOUT_SEC).setValue(accountNumber);
        $(byTagName("h3")).shouldBe(visible, TIMEOUT_SEC).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byCssSelector("div.paymentMethod  ul")).find(byText("Zenith Bank")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);

        // check that displayed additional fields for Zenith Bank PSP
        setBankFields();
        $(byCssSelector("button.btn-success")).click();
        createAndApplyTransactionsButton();
        $(byId("confirmAndApplyButton")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);

        // check that created transaction has success status
        clickTransactionsTab();
        checkTransaction(accountNumber, randomAmountStr, "Reconciled");
    }

    @Test
    void makeWithdrawalCommonwealth(){
        String accountNumber = REGULAR;
        String randomAmountStr = getRandomAmount();

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).shouldBe(visible, TIMEOUT_SEC).setValue(accountNumber);
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byTagName("h3")).shouldBe(visible, TIMEOUT_SEC).click();
        $(byId("bankName")).shouldBe(visible);
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("div.paymentMethod button")).click();
        $(byCssSelector("div.paymentMethod  ul"))
                .find(byText("Commonwealth Bank of Australia (CBA)")).click();

        // check that displayed additional fields for Zenith Bank PSP
        setBankFields();
        $(byCssSelector("button.btn-success")).click();
        createAndApplyTransactionsButton();
        $(byId("confirmAndApplyButton")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);

        // check that created transaction has success status
        clickTransactionsTab();
        checkTransaction(accountNumber, randomAmountStr, "Reconciled");
    }
}