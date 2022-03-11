package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    private ElementsCollection cards = $$(".list__item [data-test-id]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement increase = $("[data-test-id=action-deposit]");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

}
