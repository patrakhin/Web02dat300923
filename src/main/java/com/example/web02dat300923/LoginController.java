package com.example.web02dat300923;

import com.example.web02dat300923.storage.UserTokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class LoginController {

    private final UserTokenStorage userTokenStorage;

    @Autowired
    public LoginController(UserTokenStorage userTokenStorage) {
        this.userTokenStorage = userTokenStorage;
    }

    @Value("${backend.url}") // URL бэкенда, указанный в application.properties
    private String backendUrl;


    @GetMapping("/login")
    public String loginPage() {
        return "login_page";
    }

    @PostMapping("/auth/login")
    public String performLogin(String username, String password, Model model) {
        // Формируем JSON-запрос
        String jsonRequest = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        // Устанавливаем заголовки запроса
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Отправляем запрос на бэкенд
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(backendUrl + "/auth/login", HttpMethod.POST, request, String.class);

        // Обрабатываем ответ от бэкенда
        if (response.getStatusCode().is2xxSuccessful()) {
            // Если запрос успешен, извлекаем токен с использованием регулярного выражения
            String responseBody = response.getBody();
            System.out.println(responseBody);
            String jwtToken = extractToken(responseBody);
            System.out.println(jwtToken);

            // Проверяем, успешно ли удалось извлечь токен
            if (jwtToken != null) {
                // Сохраняем токен в списке
                userTokenStorage.addToken(username, jwtToken);

                // Перенаправляем пользователя на страницу manager1
                return "redirect:/manager1";
            }
        }
        // Если запрос неуспешен или токен не был получен, обрабатываем ошибку
        return "error";
    }

    // Метод для извлечения токена с использованием регулярного выражения
    private String extractToken(String responseBody) {
        Pattern pattern = Pattern.compile("\"jwt-token\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}

