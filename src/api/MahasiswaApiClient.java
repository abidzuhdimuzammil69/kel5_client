package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Mahasiswa;

public class MahasiswaApiClient {
    private static final String BASE_URL = "http://localhost/application-tier-phps/public/mahasiswa";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Mahasiswa> findAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ApiResponse<List<Mahasiswa>> apiResp = gson.fromJson(response.body(),
                new TypeToken<ApiResponse<List<Mahasiswa>>>() {
                }.getType());
        if (!apiResp.success)
            throw new Exception(apiResp.message);
        return apiResp.data;
    }

    public void create(Mahasiswa m) throws Exception {
        String json = gson.toJson(m);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("HTTP " + response.statusCode() + ": " + extractErrorMessage(response.body()));
        }
        System.out.println("Raw response:\n" + response.body());
        ApiResponse<?> apiResp = gson.fromJson(response.body(), ApiResponse.class);
        if (!apiResp.success)
            throw new Exception(apiResp.message);
    }

    public void update(Mahasiswa m) throws Exception {
        var requestBody = new HashMap<String, Object>();
        requestBody.put("nim", m.getNim());
        requestBody.put("nama", m.getNama());
        requestBody.put("jurusan", m.getJurusan() != null ? m.getJurusan() : null);
        String json = gson.toJson(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + m.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Raw response:\n" + response.body());
        handleResponse(response);
    }

    public void delete(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Raw response:\n" + response.body());
        handleResponse(response);
    }

    private static class ApiResponse<T> {
        boolean success;
        T data;
        String message;
    }

    private void handleResponse(HttpResponse<String> response) throws Exception{
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("HTTP " + response.statusCode() + ": " + extractErrorMessage(response.body()));
        }
        ApiResponse<?> apiResp = gson.fromJson(response.body(), ApiResponse.class);
        if (!apiResp.success)
            throw new Exception(apiResp.message);
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
