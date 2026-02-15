package com.EFSRTIII.ventacursos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Map;

@Service
public class PaypalService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    private final String BASE_URL = "https://api-m.sandbox.paypal.com"; // URL de pruebas

    // Método para obtener el Token de acceso (OAuth2)
    private String getAccessToken() {
        RestClient restClient = RestClient.create();

        return restClient.post()
                .uri(BASE_URL + "/v1/oauth2/token")
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString((clientId + ":" + clientSecret).getBytes()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("grant_type=client_credentials")
                .retrieve()
                .body(Map.class)
                .get("access_token").toString();
    }

    // Capturar/verificar el pago
    public boolean capturarPago(String orderId) {
        try {
            String token = getAccessToken();
            RestClient restClient = RestClient.create();

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.post()
                    .uri(BASE_URL + "/v2/checkout/orders/" + orderId + "/capture")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(Map.class);

            // Verificamos si el estado de la orden en PayPal es 'COMPLETED'
            return "COMPLETED".equals(response.get("status"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verificar el estado de una orden (sin capturarla)
    public boolean verificarPago(String orderId) {
        try {
            String token = getAccessToken();
            RestClient restClient = RestClient.create();

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.get()
                    .uri(BASE_URL + "/v2/checkout/orders/" + orderId)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(Map.class);

            return "COMPLETED".equals(response.get("status"));
        } catch (Exception e) {
            return false;
        }
    }
}