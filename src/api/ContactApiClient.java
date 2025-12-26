package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Contact;

public class ContactApiClient {
    private static final String BASE_URL;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    static {
        String baseUrl = "http://localhost/application-tier-php/public/contact";
        try {
            Properties props = new Properties();
            InputStream input = ContactApiClient.class
                .getResourceAsStream("/config.properties");
            if (input != null) {
                props.load(input);
                baseUrl = props.getProperty("api.base.url", baseUrl);
            }
        } catch (Exception e) {
            System.err.println("Failed to load config.properties: " + e.getMessage());
        }
        BASE_URL = baseUrl;
    }

    public List<Contact> findAll() throws Exception {
        return findAll(null);
    }

    public List<Contact> findAll(String searchKeyword) throws Exception {
        String url = BASE_URL;
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            url += "?search=" + java.net.URLEncoder.encode(searchKeyword.trim(), "UTF-8");
        }
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ApiResponse<List<Contact>> apiResp = gson.fromJson(response.body(),
                new TypeToken<ApiResponse<List<Contact>>>() {}.getType());
        
        if (!apiResp.success) {
            throw new Exception(apiResp.message);
        }
        return apiResp.data;
    }

    public void create(Contact contact) throws Exception {
        String json = gson.toJson(contact);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
    }

    public void update(Contact contact) throws Exception {
        var requestBody = new HashMap<String, Object>();
        requestBody.put("name", contact.getName());
        requestBody.put("phone", contact.getPhone());
        requestBody.put("email", contact.getEmail() != null ? contact.getEmail() : null);
        
        String json = gson.toJson(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + contact.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
    }

    public void delete(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse(response);
    }

    private static class ApiResponse<T> {
        boolean success;
        T data;
        String message;
    }

    private void handleResponse(HttpResponse<String> response) throws Exception {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("HTTP " + response.statusCode() + ": " + extractErrorMessage(response.body()));
        }
        
        ApiResponse<?> apiResp = gson.fromJson(response.body(), ApiResponse.class);
        if (!apiResp.success) {
            throw new Exception(apiResp.message);
        }
    }

    private String extractErrorMessage(String body) {
        try {
            ApiResponse<?> resp = gson.fromJson(body, ApiResponse.class);
            return resp.message != null ? resp.message : "Unknown server error";
        } catch (Exception e) {
            return "Server returned invalid response: " + body;
        }
    }
}