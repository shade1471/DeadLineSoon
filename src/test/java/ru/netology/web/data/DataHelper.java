package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

import static ru.netology.web.utils.SQLRequest.*;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;

        public static AuthInfo getAuthInfoExist(int number) {
            return new AuthInfo(getLoginExistUser(number), getPassword(number));
        }

        public static String getPassword(int number) {
            if (getEncryptedPasswordExistUser(number).equals(getKnownEncryptedPassword())) {
                return "qwerty123";
            } else {
                return "123qwerty";
            }
        }

        public static AuthInfo getAuthInfoInvalid() {
            return new AuthInfo("vasya", "qwerty");
        }

        public static AuthInfo getNewAuthInfo() {
            return new AuthInfo(DataHelper.AuthInfo.generateLogin("en"), "qwerty123");
        }

        public static String generateLogin(String locale) {
            String login = new Faker(new Locale(locale)).name().username();
            return login;
        }
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getWrongCode() {
        return new VerificationCode("11111");
    }
}
