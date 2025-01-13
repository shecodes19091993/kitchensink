package com.mongodb.kitchensink.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("kitchensink")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private MemberService memberService;

//    @Autowired
//    private Member newMember;

    @GetMapping("/test")
    @ResponseBody
    public String testEndpoint() {
        System.out.println("yeahhhh");
        return "This is the test endpoint!";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        logger.info("Showing registration form to user ");
        return "register";
    }
    @RequestMapping(value = "/registerMember", method = RequestMethod.POST)
    public String register(@Valid Member newMember, Model model) {
        try {


            memberService.register(newMember); // Call the register method
            model.addAttribute("successMessage", "Registration successful");
            model.addAttribute("Id",newMember.getId());
            model.addAttribute("Name",newMember.getName());
            model.addAttribute("Email",newMember.getEmail());
            model.addAttribute("Phone Number",newMember.getPhoneNumber());
            logger.info("Registering user with details"+newMember.getName());
        } catch (Exception e) {
            logger.error("Registration failed "+e.getMessage());
            model.addAttribute("failureMessage", "Registration unsuccessful: Please retry again ! ");
        }
        return "membersRegistration";
    }
    @GetMapping("/members")
    public String getAllMembers(Model model) {
        List<Member> members = memberService.findAllOrderedByName();
        model.addAttribute("members", members);
        return "members";
    }

    @GetMapping("/search/members/")
    public String findById( @RequestParam(value = "id", required = false) String id, Model model) {
        Member memberObj = new Member();
        try {

                Optional<Member> member = memberService.findById(id);
                if (member != null) {
                    memberObj = member.get();
                    model.addAttribute("successMessage", "User found !");
                    model.addAttribute("member", memberObj);
                    model.addAttribute("id", memberObj.getId());
                } else {
                    model.addAttribute("failureMessage", "Member not found");
                }

        } catch (Exception e) {
             model.addAttribute("failureMessage", "Member not found: "+e.getMessage());
        }
        return "searchMember";
    }

}
