package com.example.n5_qlsv_admin.controller;

import com.example.n5_qlsv_admin.model.SinhVien;
import com.example.n5_qlsv_admin.service.ChuyenNganhService;
import com.example.n5_qlsv_admin.service.KhoaService;
import com.example.n5_qlsv_admin.service.LopHocService;
import com.example.n5_qlsv_admin.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/sinhVien")
public class SinhVienController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private ChuyenNganhService chuyenNganhService;

    @Autowired
    private KhoaService khoaService;

    @Autowired
    private LopHocService lopHocService;

    @GetMapping
    String danhSachSinhVien(Model theModel, @RequestParam(defaultValue = "0") int pageIndex,
                            String keyword, Long mk, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        SinhVien sinhVien = sinhVienService.findById(loginedUser.getUsername());
        theModel.addAttribute("tensinhvien", sinhVien.getTenSV());

        int pageSize = 5;
        int totalPage = 0;
        int count = 0;

        if (keyword != null && keyword != "") {
            count = sinhVienService.search(keyword, 0, 0).size();
        } else if (mk != null) {
            count = sinhVienService.findAllSinhViensByKhoa(mk, 0, 0).size();
        } else {
            count = 5;
        }

        if (count % pageSize == 0) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }

        if (pageIndex < 0) {
            pageIndex = 0;
        } else if (pageIndex > totalPage - 1) {
            pageIndex = totalPage - 1;
        }

        if (mk != null) {
            theModel.addAttribute("sinhViens", sinhVienService.findAllSinhViensByKhoa(mk, pageIndex, pageSize));
        } else if (keyword != null && keyword != "") {
            theModel.addAttribute("sinhViens", sinhVienService.search(keyword, pageIndex, pageSize));
        } else {
            theModel.addAttribute("sinhViens", sinhVienService.getAllSinhViens().subList(
                    sinhVienService.getAllSinhViens().size() - 5,
                    sinhVienService.getAllSinhViens().size()
            ));
        }

        theModel.addAttribute("mk", mk);
        theModel.addAttribute("keyword", keyword);

        theModel.addAttribute("totalPage", totalPage);
        theModel.addAttribute("currentPage", pageIndex);

        theModel.addAttribute("chuyenNganhs", chuyenNganhService.getAllChuyenNganhs());
        theModel.addAttribute("khoas", khoaService.getAllKhoas());
        theModel.addAttribute("lopHocs", lopHocService.getAllLopHocs());
        theModel.addAttribute("sinhVien", new SinhVien());

        return "sinhvien";
    }

    @PostMapping
    String luuThongTinSV(SinhVien sinhVien, RedirectAttributes redirectAttributes) {
        try {
            sinhVienService.saveSinhVien(sinhVien);
            redirectAttributes.addFlashAttribute("mess", "L??u th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mess", "???? c?? l???i x???y ra");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/sinhVien";
    }

    @ResponseBody
    @GetMapping("/findSV")
    SinhVien findSV(String id) {
        return sinhVienService.findById(id);
    }

    @GetMapping(value = "/deleteSinhViens")
    String deleteSinhViens(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        try {
            String[] ma_svs = req.getParameterValues("idSV");
            if (ma_svs != null) {
                for (String ma_sv : ma_svs) {
                    sinhVienService.deleteSinhVien(ma_sv);
                }
                redirectAttributes.addFlashAttribute("mess", "X??a sinh vi??n th??nh c??ng");
                redirectAttributes.addFlashAttribute("suc_err", "success");
            } else {
                redirectAttributes.addFlashAttribute("mess", "B???n ch??a ch???n d??ng ????? x??a");
                redirectAttributes.addFlashAttribute("suc_err", "warning");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mess", "Sinh vi??n n??y ???? ????ng k?? l???p h???c ph???n, kh??ng th??? x??a");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/sinhVien";
    }

    @PostMapping("/upload")
    String saveObjectsByFile(MultipartFile fileUpload, RedirectAttributes redirectAttributes) {
        try {
            sinhVienService.uploadFile(fileUpload);
            redirectAttributes.addFlashAttribute("mess", "T???i file l??n th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mess", "Sai ?????nh d???ng file (.xlsx) ho???c l???n h??n 5MB");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }
        return "redirect:/sinhVien";

    }
}
