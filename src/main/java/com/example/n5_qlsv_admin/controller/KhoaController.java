package com.example.n5_qlsv_admin.controller;


import com.example.n5_qlsv_admin.model.Khoa;
import com.example.n5_qlsv_admin.model.SinhVien;
import com.example.n5_qlsv_admin.service.KhoaService;
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
@RequestMapping("/khoa")
public class KhoaController {

    @Autowired
    private KhoaService khoaService;

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping
    public String danhSachKhoa(Model model, @RequestParam(defaultValue = "0") int pageIndex
            , Principal principal){

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        SinhVien sinhVien = sinhVienService.findById(loginedUser.getUsername());
        model.addAttribute("tensinhvien", sinhVien.getTenSV());

        int pageSize = 8;
        int totalPage = 0;
        int count = khoaService.getAllKhoas().size();

        if (count % pageSize == 0){
            totalPage = count / pageSize;
        }else {
            totalPage = count / pageSize + 1;
        }

        if (pageIndex < 0){
            pageIndex = 0;
        }else if (pageIndex > totalPage - 1){
            pageIndex = totalPage - 1;
        }

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", pageIndex);

        model.addAttribute("khoas", khoaService.getAllKhoasByPageAndSize(pageIndex, pageSize));
        model.addAttribute("khoa", new Khoa());

        return "khoa";
    }

    @PostMapping
    public String luuThongTinKhoa(Khoa khoa, RedirectAttributes redirectAttributes){
        try{
            khoaService.saveKhoa(khoa);
            redirectAttributes.addFlashAttribute("mess", "L??u th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "???? c?? l???i x???y ra");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/khoa";
    }

    @ResponseBody
    @GetMapping("/findKhoa")
    public Khoa findKhoa(long maKhoa){
        return khoaService.findById(maKhoa);
    }

    @GetMapping("/deleteKhoas")
    public String deleteKhoa(HttpServletRequest request, RedirectAttributes redirectAttributes){
        try{
            String[] ma_khoas = request.getParameterValues("maKhoa");
            if (ma_khoas != null){
                for (String ma_khoa : ma_khoas){
                    khoaService.deleteKhoas(Long.parseLong(ma_khoa));
                }
                redirectAttributes.addFlashAttribute("mess", "X??a khoa th??nh c??ng");
                redirectAttributes.addFlashAttribute("suc_err", "success");
            }else {
                redirectAttributes.addFlashAttribute("mess", "B???n ch??a ch???n d??ng ????? x??a");
                redirectAttributes.addFlashAttribute("suc_err", "warning");
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "Khoa n???m trong chuy??n ng??nh, sinh vi??n ho???c gi???ng vi??n, kh??ng th??? x??a");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/khoa";
    }

    @PostMapping("/upload")
    String saveObjectsByFile(MultipartFile fileUpload, RedirectAttributes redirectAttributes){
        try {
            khoaService.uploadFile(fileUpload);
            redirectAttributes.addFlashAttribute("mess", "T???i file l??n th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "Sai ?????nh d???ng file (.xlsx) ho???c l???n h??n 5MB");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }
        return "redirect:/khoa";

    }
}
