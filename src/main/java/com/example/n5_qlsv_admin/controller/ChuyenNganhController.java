package com.example.n5_qlsv_admin.controller;

import com.example.n5_qlsv_admin.model.ChuyenNganh;
import com.example.n5_qlsv_admin.model.SinhVien;
import com.example.n5_qlsv_admin.service.ChuyenNganhService;
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
@RequestMapping("/chuyenNganh")
public class ChuyenNganhController {

    @Autowired
    private ChuyenNganhService chuyenNganhService;

    @Autowired
    private KhoaService khoaService;

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping
    String danhSachChuyenNganh(Model theModel, @RequestParam(defaultValue = "0") int pageIndex
            , Long mk, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        SinhVien sinhVien = sinhVienService.findById(loginedUser.getUsername());
        theModel.addAttribute("tensinhvien", sinhVien.getTenSV());

        int pageSize = 5;
        int totalPage = 0;
        int count = 0;

        if(mk != null){
            count = chuyenNganhService.findAllChuyenNganhsByKhoa(mk, 0, 0).size();
        }else {
            count = 5;
        }

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

        if(mk != null){
            theModel.addAttribute("chuyenNganhs",
                    chuyenNganhService.findAllChuyenNganhsByKhoa(mk, pageIndex, pageSize));
        }else {
            theModel.addAttribute("chuyenNganhs",
                    chuyenNganhService.getAllChuyenNganhs().subList(
                            chuyenNganhService.getAllChuyenNganhs().size() - 5,
                            chuyenNganhService.getAllChuyenNganhs().size()
                    ));
        }

        theModel.addAttribute("mk", mk);

        theModel.addAttribute("totalPage", totalPage);
        theModel.addAttribute("currentPage", pageIndex);

        theModel.addAttribute("khoas", khoaService.getAllKhoas());
        theModel.addAttribute("chuyenNganh", new ChuyenNganh());

        return "chuyennganh";
    }

    @PostMapping
    String luuThongTinChuyenNganh(ChuyenNganh chuyenNganh, RedirectAttributes redirectAttributes) {
        try{
            chuyenNganhService.saveChuyenNganh(chuyenNganh);
            redirectAttributes.addFlashAttribute("mess", "L??u th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "???? c?? l???i x???y ra");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/chuyenNganh";
    }

    @ResponseBody
    @GetMapping("/findCN")
    ChuyenNganh findHK(long id){
        return chuyenNganhService.findById(id);
    }

    @GetMapping(value = "/deleteChuyenNganhs")
    String deleteHocKys(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        try{
            String[] ids = req.getParameterValues("idCN");
            if (ids != null) {
                for (String id : ids) {
                    chuyenNganhService.deleteChuyenNganh(Long.parseLong(id));
                }
                redirectAttributes.addFlashAttribute("mess", "X??a sinh vi??n th??nh c??ng");
                redirectAttributes.addFlashAttribute("suc_err", "success");
            }else {
                redirectAttributes.addFlashAttribute("mess", "B???n ch??a ch???n d??ng ????? x??a");
                redirectAttributes.addFlashAttribute("suc_err", "warning");
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "Sinh vi??n n??y ???? ????ng k?? l???p h???c ph???n, kh??ng th??? x??a");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/chuyenNganh";
    }

    @PostMapping("/upload")
    String saveObjectsByFile(MultipartFile fileUpload, RedirectAttributes redirectAttributes){
        try {
            chuyenNganhService.uploadFile(fileUpload);
            redirectAttributes.addFlashAttribute("mess", "T???i file l??n th??nh c??ng");
            redirectAttributes.addFlashAttribute("suc_err", "success");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mess", "Sai ?????nh d???ng file (.xlsx) ho???c l???n h??n 5MB");
            redirectAttributes.addFlashAttribute("suc_err", "error");
        }

        return "redirect:/chuyenNganh";
    }
}