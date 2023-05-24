package ru.yandex.prakticum.OrderT;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.notNullValue;

public class ListOfOrdersTest {
    private CourierClient courierClient;
    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Проверить: в теле список заказов, ответ 200")//имя теста
    @Description("GET запрос на ручку /api/v1/orders")//описание теста
    @Step("Основной шаг - тест")
    public void getListOfOrder(){
        ValidatableResponse getResponse = courierClient.getListOfOrder();
        getResponse.assertThat().statusCode(HTTP_OK)
                .and()
                .assertThat().body("orders.id",notNullValue());
    }

}
