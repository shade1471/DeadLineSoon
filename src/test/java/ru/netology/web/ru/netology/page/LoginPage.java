package ru.netology.web.ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public void dataInput(String login, String pass) {
        loginField.setValue(login);
        passwordField.setValue(pass);
        loginButton.click();
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        dataInput(info.getLogin(), info.getPassword());
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.AuthInfo info) {
        dataInput(info.getLogin(), info.getPassword());
        errorNotification.shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    public void invalidLoginMoreThreeTimes(DataHelper.AuthInfo info) {
        dataInput(info.getLogin(), info.getPassword());
        loginButton.click();
        loginButton.click();
//        errorNotification.shouldHave(text("Неверно указан код! Попробуйте ещё раз."));
    }
}
