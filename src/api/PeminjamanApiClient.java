package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Peminjaman;
import model.Buku;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public class PeminjamanApiClient {
    private static final String BASE_URL = "http://localhost/application-tier-php/public/api";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .setDateFormat("yyyy-MM-dd")
            .create();

    // ========== PEMINJAMAN CRUD ==========
    
    public List<Peminjaman> findAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/peminjaman"))
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("GET Response: " + response.body());
        
        ApiResponse<List<Peminjaman>> apiResp = gson.fromJson(response.body(),
                new TypeToken<ApiResponse<List<Peminjaman>>>() {}.getType());
        
        if (!apiResp.isSuccess())
            throw new Exception(apiResp.getMessage());
        
        return apiResp.getData();
    }

    public Peminjaman findById(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/peminjaman/" + id))
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("GET by ID Response: " + response.body());
        
        ApiResponse<Peminjaman> apiResp = gson.fromJson(response.body(),
                new TypeToken<ApiResponse<Peminjaman>>() {}.getType());
        
        if (!apiResp.isSuccess())
            throw new Exception(apiResp.getMessage());
        
        return apiResp.getData();
    }

    public void create(Peminjaman peminjaman) throws Exception {
        String json = gson.toJson(peminjaman);
        System.out.println("Creating peminjaman: " + json);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/peminjaman"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Create Response: " + response.body());
        
        handleResponse(response);
    }

    public void update(Peminjaman peminjaman) throws Exception {
        String json = gson.toJson(peminjaman);
        System.out.println("Updating peminjaman: " + json);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/peminjaman/" + peminjaman.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Update Response: " + response.body());
        
        handleResponse(response);
    }

    public void delete(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/peminjaman/" + id))
                .DELETE()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Delete Response: " + response.body());
        
        handleResponse(response);
    }

    // ========== BUKU ==========
    
    public List<Buku> findAllBuku() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/buku"))
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("GET Buku Response: " + response.body());
        
        ApiResponse<List<Buku>> apiResp = gson.fromJson(response.body(),
                new TypeToken<ApiResponse<List<Buku>>>() {}.getType());
        
        if (!apiResp.isSuccess())
            throw new Exception(apiResp.getMessage());
        
        return apiResp.getData();
    }

    // ========== HELPER METHODS ==========
    
    private void handleResponse(HttpResponse<String> response) throws Exception {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("HTTP " + response.statusCode() + ": " + extractErrorMessage(response.body()));
        }
        
        ApiResponse<?> apiResp = gson.fromJson(response.body(), ApiResponse.class);
        if (!apiResp.isSuccess())
            throw new Exception(apiResp.getMessage());
    }

    private String extractErrorMessage(String body) {
        try {
            ApiResponse<?> resp = gson.fromJson(body, ApiResponse.class);
            return resp.getMessage() != null ? resp.getMessage() : "Unknown server error";
        } catch (Exception e) {
            return "Server returned invalid response: " + body;
        }
    }
}