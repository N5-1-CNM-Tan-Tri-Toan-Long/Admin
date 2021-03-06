package com.example.n5_qlsv_admin.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HocPhan {

    private String maHocPhan;

    private boolean hocPhanBatBuoc;

    private int soTCLT;

    private int soTCTH;

    private MonHoc monHoc;

    private ChuyenNganh chuyenNganh;

    private List<LopHocPhan> lopHocPhanList;

}
