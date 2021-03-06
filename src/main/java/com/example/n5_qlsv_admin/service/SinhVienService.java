package com.example.n5_qlsv_admin.service;

import com.example.n5_qlsv_admin.model.SinhVien;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SinhVienService {

    List<SinhVien> getAllSinhViens();

    List<SinhVien> getAllSinhViensByPageAndSize(int pageIndex, int pageSize);

    void saveSinhVien(SinhVien sinhVien);

    void deleteSinhVien(String ma_sv);

    SinhVien findById(String ma_sv);

    List<SinhVien> search(String keyword, int pageIndex, int pageSize);

    List<SinhVien> findAllSinhViensByKhoa(Long maKhoa, int pageIndex, int pageSize);

    void uploadFile(MultipartFile file);
}
