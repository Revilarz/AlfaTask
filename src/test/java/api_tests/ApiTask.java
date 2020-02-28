package api_tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *Сформируйте проект с тестами, который будет вызывать API Яндекс.Погоды для города
 * Москва. Прогноз погоды запрашивайте всего на два дня. Получив ответ от API сделайте
 * следующие проверки:
 * 1. lat - широта (в градусах) соответствует заданной вами;
 * 2. lon - долгота (в градусах) соответствует заданной вами;
 * 3. offset - проверьте часовой пояс;
 * 4. name - проверьте название часового пояса;
 * 5. abbr - проверьте сокращенное название часового пояса;
 * 6. dst - проверьте признак летнего времени;
 * 7. url - проверьте страницу населенного пункта, убедитесь что ссылка правильная, что
 * широта и долгота внутри ссылки указаны верные;
 * 8. Проверьте длину прогноза, убедитесь что прогноз действительно на два дня;
 * 9. season - проверьте сезон;
 * 10. Напишите логику и проверьте, что код фазы луны на второй день moon_code
 * соответсвует текстовому коду moon_text.
 */
public class ApiTask {

    /**
     * Переменная для широты
     * (для Assert для универсальности и удобсвта восприятия)
     */
    private static String lat = "55.75396";

    /**
     * Долгота
     */
    private static String lon = "37.62039";

    /**
     * Часовой пояс в секундах
     */
    private static String offset = "10800";


    /**
     * Название часового пояса
     */
    private static String nameTimeZone = "Europe/Moscow";


    /**
     * Сокращенное название часового пояса
     */
    private static String abbreviationTimeZone = "MSK";


    /**
     * Признак летнего времени
     */
    private static boolean signOfSummerTime = false;


    /**
     * Страница населенного пункта на сайте Яндекс.Погода
     */
    private static String urlWithCoordinates = "https://yandex.ru/pogoda/?lat=55.75396&lon=37.62039";


    /**
     * Срок прогноза (указываемые в запросе)
     */
    private static int limit = 2;


    /**
     * Метод для отправки GET запроса
     */
    public static JsonPath jsonPathValidator() {

        Response response = RestAssured.given()
                .header("X-Yandex-API-Key", "76d673c0-5620-488b-967f-b59329a12fbb")
                .when()
                .get("https://api.weather.yandex.ru/v1/forecast?lat=55.75396&lon=37.62039&limit=2");

        return response.jsonPath();

    }

    /**
     * 1. lat - широта (в градусах) соответствует заданной вами;
     */
    @Test
    public void checkLat() {
        Assert.assertEquals(jsonPathValidator().get("info.lat").toString(), lat);
    }

    /**
     * 2. lon - долгота (в градусах) соответствует заданной вами;
     */
    @Test
    public void checkLon() {
        Assert.assertEquals(jsonPathValidator().get("info.lon").toString(), lon);
    }

    /**
     * 3. offset - проверьте часовой пояс;
     */
    @Test
    public void checkOffsetTimeZone() {
        Assert.assertEquals(jsonPathValidator().get("info.tzinfo.offset").toString(), offset);
    }

    /**
     * 4. name - проверьте название часового пояса;
     */
    @Test
    public void checkNameTimeZone() {
        Assert.assertEquals(jsonPathValidator().get("info.tzinfo.name"), nameTimeZone);
    }

    /**
     * 5. abbr - проверьте сокращенное название часового пояса;
     */
    @Test
    public void checkTimeZoneInfoAbbreviation() {
        Assert.assertEquals(jsonPathValidator().get("info.tzinfo.abbr"), abbreviationTimeZone);
    }

    /**
     * 6. dst - проверьте признак летнего времени;
     */
    @Test
    public void checkTimeZoneInfoSignOfSummerTime() {
        Assert.assertEquals(jsonPathValidator().get("info.tzinfo.dst"), signOfSummerTime);
    }

    /**
     * 7. url - проверьте страницу населенного пункта, убедитесь что ссылка правильная, что
     * широта и долгота внутри ссылки указаны верные;
     */
    @Test
    public void checkInfoUrl() {
        Assert.assertEquals(jsonPathValidator().get("info.url"), urlWithCoordinates);
    }

    /**
     * 8. Проверьте длину прогноза, убедитесь что прогноз действительно на два дня;
     */
    @Test
    public void checkSumDays() {
        List<String> jsonResponse = jsonPathValidator().get("forecasts.date");
        Assert.assertEquals(jsonResponse.size(), limit);
    }

    /**
     * 9. season - проверьте сезон;
     */
    @Test
    public void checkSeason() {
        DateFormat dateFormat = new SimpleDateFormat("M");
        Date date = new Date();
        int month = Integer.parseInt(dateFormat.format(date));

        if (month >= 3 & month <= 5) {
            Assert.assertEquals(jsonPathValidator().get("fact.season").toString(), "spring");
        } else {
            if (month >= 6 & month <= 8) {
                Assert.assertEquals(jsonPathValidator().get("fact.season").toString(), "summer");
            } else {
                if (month >= 9 & month <= 11) {
                    Assert.assertEquals(jsonPathValidator().get("fact.season").toString(), "autumn");
                } else {
                    Assert.assertEquals(jsonPathValidator().get("fact.season").toString(), "winter");
                }
            }
        }
    }

    /**
     * 10. Напишите логику и проверьте, что код фазы луны на второй день moon_code
     * соответсвует текстовому коду moon_text.
     */
    @Test
    public void checkMoonCodeAndMoonText() {

    }


}