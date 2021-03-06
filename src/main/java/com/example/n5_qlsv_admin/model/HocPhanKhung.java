package com.example.n5_qlsv_admin.model;

import lombok.*;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HocPhanKhung {

    private long maHPK;

    private Integer soTietLT, soTietTH, thuTuHocKy;

    private String trangThai;

    private HocPhan hocPhan;

}
