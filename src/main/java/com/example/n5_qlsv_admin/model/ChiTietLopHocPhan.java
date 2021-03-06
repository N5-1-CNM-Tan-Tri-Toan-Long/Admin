package com.example.n5_qlsv_admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChiTietLopHocPhan implements Serializable {

    private long maCTLHP;

    private String moTa;

    private String phong;

    private String dayNha;

    private String coSo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayBatDau;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayKetThuc;

    private String tietHoc;

    private Integer nhomTH;

    private GiangVien giangVien;

    private LopHocPhan lopHocPhan;

    private List<LichHocSinhVien> lichHocSinhVienList;
}
