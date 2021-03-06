package com.example.n5_qlsv_admin.controller;

import com.example.n5_qlsv_admin.model.HocKy;
import com.example.n5_qlsv_admin.model.SinhVien;
import com.example.n5_qlsv_admin.service.HocKyService;
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
@RequestMapping("/hocKy")
public class HocKyController {

    @Autowired
    private HocKyService hocKyService;

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping
    String danhSachHocKy(Model theModel, @RequestParam(defaultValue = "0") int pageIndex, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        SinhVien sinhVien = sinhVienService.findById(loginedUser.getUsername());
        theModel.addAttribute("tensinhvien", sinhVien.getTenSV());

        int pageSize = 8;
        int totalPage = 0;
        int count = hocKyService.getAllHocKys().size();

        if (count % pageSize == 0) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }

        if(pageIndex < 0){
            pageIndex = 0;
        }else if(pageIndex > totalPage - 1){
            pageIndex = totalPage - 1;
        }

        theModel.addAttribute("totalPage", totalPage);
        theModel.addAttribute("currentPage", pageIndex);

        theModel.addAttribute("hocKys", hocKyService.getAllHocKysByPageAndSize(pageIndex, pageSize));
        theModel.addAttribute("hocKy", new HocKy());

        return "hocky";
    }

    @PostMapping
    String luuThongTinHK(HocKy hocKy, RedirectAttributes redirectAttributes) {
        try{
            hocKyService.saveHocKy(hocKy);
            redirectAttributes.addFlashAttribute("mess", "L??u th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "???? c?? l???i x???y ra");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }
        return "redirect:/hocKy";
    }

    @ResponseBody
    @GetMapping("/findHK")
    HocKy findHK(long id){
        return hocKyService.findById(id);
    }

    @GetMapping(value = "/deleteHocKys")
    String deleteHocKys(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        try{
            String[] ids = req.getParameterValues("idHK");
            if (ids != null) {
                for (String id : ids) {
                    hocKyService.deleteHocKy(Long.parseLong(id));
                }
                redirectAttributes.addFlashAttribute("mess", "X??a h???c k??? th??nh c??ng");
                redirectAttributes.addFlashAttribute("suc_err", "success");
            }else {
                redirectAttributes.addFlashAttribute("mess", "B???n ch??a ch???n d??ng ????? x??a");
                redirectAttributes.addFlashAttribute("suc_err", "warning");
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "H???c k??? ???? c?? trong l???p h???c ph???n, kh??ng th??? x??a");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/hocKy";
    }

    @PostMapping("/upload")
    String saveObjectsByFile(MultipartFile fileUpload, RedirectAttributes redirectAttributes){
        try {
            hocKyService.uploadFile(fileUpload);
            redirectAttributes.addFlashAttribute("mess", "T???i file l??n th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "Sai ?????nh d???ng file (.xlsx) ho???c l???n h??n 5MB");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }
        return "redirect:/hocKy";

    }
}
