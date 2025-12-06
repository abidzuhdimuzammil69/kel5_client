package service;

import java.util.List;

import api.MahasiswaApiClient;
import model.Mahasiswa;

public class MahasiswaServiceDefault implements MahasiswaService {
    private final MahasiswaApiClient apiClient = new MahasiswaApiClient();

    @Override
    public void createMahasiswa(Mahasiswa mahasiswa) {
        try {
            apiClient.create(mahasiswa);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void updateMahasiswa(Mahasiswa mahasiswa) {
        try {
            apiClient.update(mahasiswa);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteMahasiswa(Mahasiswa mahasiswa) {
        try {
            apiClient.delete(mahasiswa.getId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Mahasiswa> getAllMahasiswa() {
        try {
            return apiClient.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}