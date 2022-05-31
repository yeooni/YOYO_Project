package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/adminFood")
    public String adminFood(){
        return "admin/adminFood";
    }

    @GetMapping("/admin/adminModify")
    public String adminModify(){
        return "admin/adminModify";
    }
    @GetMapping("/admin/adminAdd")
    public String adminAdd(){
        return "admin/adminAdd";
    }

    @GetMapping("/admin/adminUser")
    public String adminUser(){
        return "admin/adminUser";
    }


}
