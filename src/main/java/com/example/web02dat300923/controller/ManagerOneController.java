package com.example.web02dat300923.controller;

import com.example.web02dat300923.dto.VehicleDTO;
import com.example.web02dat300923.storage.UserTokenStorage;
import com.example.web02dat300923.util.VehicleListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
public class ManagerOneController {
    private final UserTokenStorage userTokenStorage;

    @Autowired
    public ManagerOneController(UserTokenStorage userTokenStorage) {
        this.userTokenStorage = userTokenStorage;
    }

    @Value("${backend.url}")
    private String backendUrl;

    @GetMapping("/manager1")
    public String showManagerPage() {
        // Логика для отображения страницы manager1
        return "manager1";
    }

    @GetMapping("/vehicles")
    public String showVehicles(Model model, @RequestParam(defaultValue = "1") int page) {
        // Получаем токен пользователя из UserTokenStorage
        String token = userTokenStorage.getToken("manager1");

        // Проверяем токен
        if (token == null || token.isEmpty()) {
            // Обработка случая, когда токен отсутствует или некорректен
            // Возможно, редирект на страницу логина или другая обработка ошибки
            return "redirect:/login";
        }

        // Устанавливаем токен в заголовки запроса
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        // Подготавливаем URL с параметрами пагинации
        String url = UriComponentsBuilder.fromHttpUrl(backendUrl + "/pagination_manager1/vehicles")
                .queryParam("page", page)
                .queryParam("size", 10)
                .build().toUriString();

        // Отправляем запрос на бэкенд для получения пагинированного списка машин
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<VehicleListResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                VehicleListResponse.class
        );

        // Получаем данные от бэкенда
        VehicleListResponse vehicleListResponseStart = response.getBody();
        assert vehicleListResponseStart != null;
        int totalPages = vehicleListResponseStart.getTotalPages();
        List<VehicleDTO> vehicles = vehicleListResponseStart.getContent();

        // Передаем данные на страницу
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("currentPage", page);

        return "vehicles_all";
    }
}
