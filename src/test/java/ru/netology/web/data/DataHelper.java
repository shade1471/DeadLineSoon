package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

public class DataHelper {

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo(generateLogin("en"), "qwerty123");
    }

    public static String generateLogin(String locale) {
        String login = new Faker(new Locale(locale)).name().username();
        return login;
    }

//    public static AuthInfo getInvalidAuthInfo(AuthInfo original) {
//        return new AuthInfo(DataHelper.generateLogin("en"), DataHelper.generatePassword("en"));
//    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class Card {
        private String cardId;
        private String cardNumber;
    }

    private static Card[] cards =
            {new Card("92df3f1c-a033-48e6-8390-206f6b1f56c0", "5559 0000 0000 0001"),
                    new Card("0f3f5c2a-249e-4c3d-8287-09f7a039391d", "5559 0000 0000 0002")};

    public static Card getCard(int number) {
        return cards[number - 1];
    }

    public static Card getWrongCard() {
        return new Card("", "5559 0000 0000 1111");
    }
}
