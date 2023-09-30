package com.example.web02dat300923;

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

@Controller
public class LoginController {

    @Value("${backend.url}") // URL бэкенда, указанный в application.properties
    private String backendUrl;

    @GetMapping("/login")
    public String loginPage(){
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
            // Если запрос успешен, сохраняем токен в списке
            // Предполагается, что у вас есть список, в который вы добавляете токены
            // Например: tokenList.add(response.getBody());
            model.addAttribute("message", "Токен успешно получен: " + response.getBody());
            return "success"; // Имя страницы с сообщением об успешном получении токена
        } else {
            // Если запрос неуспешен, обрабатываем ошибку (например, выводим сообщение об ошибке на странице)
            model.addAttribute("error", "Ошибка при получении токена. Пожалуйста, попробуйте еще раз.");
            return "error"; // Имя страницы с сообщением об ошибке
        }
    }
}

