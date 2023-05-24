package ru.yandex.prakticum.OrderT;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.model.ListForCreateOrder;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List color;

    public CreateOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, List color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    private CourierClient courierClient;
    @Parameterized.Parameters
    public static Object[][] getInput(){
        return new Object[][]{
                {"Фёдор","Достоевский","Старая Басманная 21",181,"88002000600",2,"2023-03-20","Самокат не наказание",List.of("BLACK, GREY")},
                {"Владимир","Маяковский","Красная Пресня 36",30,"88002000600",1,"2023-03-21","Самокаты в облаках",List.of("BLACK")},
                {"Сергей","Есенин","Большой Строченовский 24",23,"88002000600",2,"2023-03-20","Самокат не наказание",List.of("")}
        };
    }
    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Создание заказа с параметризацией")
    @Description("Post запрос на ручку /api/v1/orders")
    @Step("Основной шаг - тест(создание заказа)")
    public void createOrder(){
        ListForCreateOrder listForCreateOrder = new ListForCreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        ValidatableResponse getOrderResponse = courierClient.createOrder(listForCreateOrder);
        getOrderResponse.assertThat().statusCode(HTTP_CREATED);
        getOrderResponse.assertThat().extract().path("track");

    }
}
