package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import okio.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

class DeliveryTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen=true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);


        $x("//input[contains(@placeholder,\"Город\")]").setValue(validUser.getCity());
        $x("//input[contains(@placeholder,\"Дата встречи\")]").doubleClick().doubleClick().sendKeys(Keys.DELETE);
        $x("//input[contains(@placeholder,\"Дата встречи\")]").setValue(firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $$("[type='tel']").last().setValue(validUser.getPhone());
        $(".checkbox").click();
        $$(".button").first().click();
        $("[data-test-id=\"success-notification\"]").shouldBe(visible)
                .shouldBe(text("Успешно!\n" + "Встреча успешно запланирована на " + firstMeetingDate));

        $x("//input[contains(@placeholder,\"Дата встречи\")]").doubleClick().doubleClick().sendKeys(Keys.DELETE);
        $x("//input[contains(@placeholder,\"Дата встречи\")]").setValue(secondMeetingDate);

        $$(".button").first().click();

        $("[data-test-id=\"replan-notification\"]")
                .shouldBe(visible).shouldBe(text("Необходимо подтверждение\n" +
                        "У вас уже запланирована встреча на другую дату. Перепланировать?"));

        $$(".button").last().click();
        $("[data-test-id=\"success-notification\"]").shouldBe(visible)
                .shouldBe(text("Успешно!\n" + "Встреча успешно запланирована на " + secondMeetingDate));

    }
}