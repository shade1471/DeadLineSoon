package ru.netology.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.data.DataHelper.AuthInfo.*;
import static ru.netology.web.utils.SQLRequest.*;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    public static void clear() {
        clearAll();
    }

    @Test
    void shouldAuthByFirstExistUser() {
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(getAuthInfoExist(1));
        verificationPage.validVerify(getVerificationCodeFor(getAuthInfoExist(1).getLogin()));
    }

    @Test
    void shouldAuthIfUserExistWithWrongPassword() {
        AuthInfo info = getAuthInfoInvalid();
        var loginPage = new LoginPage();
        loginPage.invalidLogin(info);
    }

    @Test
    void shouldAuthWithWrongCode() {
        DataHelper.AuthInfo user = getNewAuthInfo();
        addNewUser(user);
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(user);
        verificationPage.invalidVerify(getWrongCode());
    }

    @Test
    void shouldAuthByNewUser() {
        DataHelper.AuthInfo user = getNewAuthInfo();
        addNewUser(user);
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(user);
        verificationPage.validVerify(getVerificationCodeFor(user.getLogin()));
    }

    @Test
    void authWithWrongCodeMoreThreeTimes() {
        DataHelper.AuthInfo user = getNewAuthInfo();
        addNewUser(user);

        for (int i = 0; i < 3; i++) {
            open("http://localhost:9999");
            var loginPage = new LoginPage();
            var verificationPage = loginPage.validLogin(user);
            verificationPage.invalidVerify(getWrongCode());
        }
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(user);
        verificationPage.invalidVerifyMoreThreeTimes(getWrongCode());
    }
}


