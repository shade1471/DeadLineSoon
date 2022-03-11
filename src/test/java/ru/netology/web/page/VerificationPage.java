package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public VerificationPage() {
        codeField.shouldBe(visible);
    }

    public void dataInput(DataHelper.VerificationCode code) {
        codeField.setValue(code.getCode());
        verifyButton.click();
    }

    public DashboardPage validVerify(DataHelper.VerificationCode code) {
        dataInput(code);
        return new DashboardPage();
    }

    public void invalidVerify(DataHelper.VerificationCode code) {
        dataInput(code);
        errorNotification.shouldHave(text("Неверно указан код! Попробуйте ещё раз."));
    }

    @SneakyThrows
    public void invalidVerifyMoreThreeTimes(DataHelper.VerificationCode code) {
        dataInput(code);
        errorNotification.shouldHave(text("Превышено количество попыток ввода кода!"));
    }
}
