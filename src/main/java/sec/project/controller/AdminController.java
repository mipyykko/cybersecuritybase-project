/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.repository.SignupRepository;

/**
 *
 * @author pyykkomi
 */
@Controller
public class AdminController {
    
    @Autowired
    private SignupRepository signupRepository;
    
    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("signups", signupRepository.findAll());
        model.addAttribute("redirect", "/admin");
        return "admin";
    }
    
    @RequestMapping("/admin/delete/{id}")
    public String deleteSignup(Model model, @PathVariable Long id, @RequestParam(value="redirect", defaultValue="/admin") String redirect) {
        signupRepository.delete(id);
        return "redirect:" + redirect;
    }
}
