package com.example.n5_qlsv_admin.controller;

import com.example.n5_qlsv_admin.model.LopHoc;
import com.example.n5_qlsv_admin.model.SinhVien;
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
@RequestMapping("/lophoc")
public class LopHocController {

    @Autowired
    private LopHocService lopHocService;

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping
    public String danhSachLopHoc(Model model, @RequestParam(defaultValue = "0") int pageIndex, Principal principal){

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        SinhVien sinhVien = sinhVienService.findById(loginedUser.getUsername());
        model.addAttribute("tensinhvien", sinhVien.getTenSV());

        int pageSize = 8;
        int totalPage = 0;
        int count = lopHocService.getAllLopHocs().size();

        if (count % pageSize == 0){
            totalPage = count / pageSize;
        }else {
            totalPage = count / pageSize + 1;
        }

        if (pageIndex < 0) {
            pageIndex = 0;
        }else if (pageIndex > totalPage - 1){
            pageIndex = totalPage - 1;
        }

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("lophocs", lopHocService.getAllLopHocsByPageAndSize(pageIndex, pageSize));
        model.addAttribute("lophoc", new LopHoc());

        return "lophoc";
    }

    @PostMapping
    public String luuThongTinLopHoc(LopHoc lopHoc, RedirectAttributes redirectAttributes){
        try {
            lopHocService.saveLopHoc(lopHoc);
            redirectAttributes.addFlashAttribute("mess", "L??u th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "???? c?? l???i x???y ra");
            redirectAttributes.addFlashAttribute("suc_err", "warning");
        }

        return "redirect:/lophoc";
    }

    @ResponseBody
    @GetMapping("/findLopHoc")
    public LopHoc findLopHoc(long maLopHoc){
        return lopHocService.findById(maLopHoc);
    }

    @GetMapping("/deleteLopHocs")
    public String deleteLopHoc(HttpServletRequest request, RedirectAttributes redirectAttributes){
        try{
            String[] maLopHocs = request.getParameterValues("maLop");
            if (maLopHocs != null){
                for (String maLopHoc : maLopHocs){
                    lopHocService.deleteLopHocs(Long.parseLong(maLopHoc));
                }
                redirectAttributes.addFlashAttribute("mess", "X??a l???p h???c th??nh c??ng");
                redirectAttributes.addFlashAttribute("suc_err", "success");
            }else {
                redirectAttributes.addFlashAttribute("mess", "B???n ch??a ch???n d??ng ????? x??a");
                redirectAttributes.addFlashAttribute("suc_err", "warning");
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "L???p h???c ???? c?? trong sinh vi??n, kh??ng th??? x??a");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/lophoc";
    }

    @PostMapping("/upload")
    String saveObjectsByFile(MultipartFile fileUpload, RedirectAttributes redirectAttributes){
        try {
            lopHocService.uploadFile(fileUpload);
            redirectAttributes.addFlashAttribute("mess", "T???i file l??n th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "Sai ?????nh d???ng file (.xlsx) ho???c l???n h??n 5MB");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/lophoc";
    }
}
