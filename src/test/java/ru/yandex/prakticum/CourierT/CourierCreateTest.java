package ru.yandex.prakticum.CourierT;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.CourierGenerator;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.model.*;


import static java.net.HttpURLConnection.*;

public class CourierCreateTest {
    private CourierClient courierClient;
    private int courierId;
    @Before
    public void setUp() {
        courierClient = new CourierClient();

    }

    @Test
    @DisplayName("Курьера можно создать. Возвращает код ответа 201")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - тест (создание, валидность кода ответа 201)")
    public void createCourierValidateAndResponseTest(){
        Courier courier = new Courier(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN,CourierGenerator.FIRST_NAME_GEN);
        courierClient.create(courier)
                .assertThat().statusCode(HTTP_CREATED);

        CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN);
        Response response = courierClient.login(courierLogin);
        courierId = response.path("id");
    }

    @Test
    @DisplayName("Курьера можно создать. Успешный запрос возвращает: {\"ok\":true}")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - тест (создание, валидность {\"ok\":true} ")
    public void createCourierValidateTest() {
        Courier courier = new Courier(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN,CourierGenerator.FIRST_NAME_GEN);
        courierClient.create(courier)
                .assertThat().extract().path("true");

        CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN);
        Response response = courierClient.login(courierLogin);
        courierId = response.path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров.Error 409")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - тест(проверка двух одинаковых курьеров, возвращается ошибка 409 при повторяющимся логине )")
    public void twoIdenticalCouriers(){
        Courier courier = new Courier(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN,CourierGenerator.FIRST_NAME_GEN);
        ValidatableResponse createResponse = courierClient.create(courier);
        ValidatableResponse createResponseSecond = courierClient.create(courier);
        createResponseSecond.assertThat().statusCode(HTTP_CONFLICT);

        CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_GEN,CourierGenerator.PASSWORD_GEN);
        Response response = courierClient.login(courierLogin);
        courierId = response.path("id");
    }

    @Test
    @DisplayName("Создание курьера без пароля. Error 400")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание курьера")
    public void createCourierNotPassword(){
        Courier courier = new Courier(CourierGenerator.LOGIN_GEN,null,CourierGenerator.FIRST_NAME_GEN);
        courierClient.create(courier)
                .assertThat().statusCode(HTTP_BAD_REQUEST);

        try {
            CourierLogin courierLogin = new CourierLogin(CourierGenerator.LOGIN_GEN, "");
            Response response = courierClient.login(courierLogin);
            courierId = response.path("id");
        }catch (NullPointerException e){
            System.out.println("Исключение NullPointerException");
        }
    }

    @Test
    @DisplayName("Создание курьера без логина. Error 400")
    @Description("Post запрос на ручку /api/v1/courier")
    @Step("Основной шаг - создание курьера")
    public void createCourierNotLogin(){
        Courier courier = new Courier(null,CourierGenerator.PASSWORD_GEN,CourierGenerator.FIRST_NAME_GEN);
        ValidatableResponse createResponse = courierClient.create(courier);
        createResponse.assertThat().statusCode(400);
         try {
             CourierLogin courierLogin = new CourierLogin(null, CourierGenerator.PASSWORD_GEN);
             Response response = courierClient.login(courierLogin);
             courierId = response.path("id");
         }catch (NullPointerException e){
             System.out.println("Исключение NullPointerException");
         }
    }
    @After
    @Step("Постусловие.Удаление курьера")
    public void clearData() {
        courierClient.delete(courierId);
        System.out.println("удален - " + courierId);
    }
}
