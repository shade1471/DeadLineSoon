package ru.netology.web.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.*;

public class AuthTest {

    private DataHelper.AuthInfo newAuthInfo = DataHelper.getNewAuthInfo();

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldAuthForFirstExistUser() {

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(getAuthInfoExist(1));
        verificationPage.validVerify(getVerificationCodeFor(getAuthInfoExist(1).getLogin()));

    }

    @Test
    void shouldAuthForSecondExistUser() {

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(getAuthInfoExist(2));
        verificationPage.validVerify(getVerificationCodeFor(getAuthInfoExist(2).getLogin()));

    }

    @Test
    void shouldAuthIfUserExistWithWrongPassword() {
        AuthInfo info = getAuthInfoInvalid();

        var loginPage = new LoginPage();
        loginPage.invalidLogin(info);

    }

    @Test
    void shouldAuthWithWrongCode() {

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(getAuthInfoExist(2));
        verificationPage.invalidVerify(getWrongCode());

    }

    @Test
    void shouldAuthWithWrongPasswordMoreThreeTimes() {
        DataHelper.AuthInfo user = DataHelper.getNewAuthInfo();
        addUser(user);

        var loginPage = new LoginPage();
        loginPage.invalidLoginMoreThreeTimes(new AuthInfo(user.getLogin(), "qwerty"));


    }



//    @Test
//    void shouldAuthForNewUser() {
//        addUser(authInfo);
//        var loginPage = new LoginPage();
//        var verificationPage = loginPage.validLogin(authInfo);
//        verificationPage.validVerify(getVerificationCodeFor(getAuthInfoExist(2).getLogin()));
//
//    }

}


