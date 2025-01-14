package com.mongodb.kitchensink.controller;

import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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

//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("member", new Member());
//        logger.info("Showing registration form to user ");
//        return "register";
//    }
//    @RequestMapping(value = "/registerMember", method = RequestMethod.POST)
//    public String register(@Valid Member newMember, Model model) {
//        try {
//
//
//            memberService.register(newMember); // Call the register method
//            model.addAttribute("successMessage", "Registration successful");
//            model.addAttribute("Id",newMember.getId());
//            model.addAttribute("Name",newMember.getName());
//            model.addAttribute("Email",newMember.getEmail());
//            model.addAttribute("Phone Number",newMember.getPhoneNumber());
//            logger.info("Registering user with details"+newMember.getName());
//        } catch (Exception e) {
//            logger.error("Registration failed "+e.getMessage());
//            model.addAttribute("failureMessage", "Registration unsuccessful: Please retry again ! ");
//        }
//        return "membersRegistration";
//    }
//    @GetMapping("/members")
//    public String getAllMembers(Model model) {
//        List<Member> members = memberService.findAllOrderedByName();
//        model.addAttribute("members", members);
//        return "members";
//    }

//    @GetMapping("/search/members/")
//    public String findById( @RequestParam(value = "id", required = false) String id, Model model) {
//        Member memberObj = new Member();
//        try {
//
//                Optional<Member> member = memberService.findById(id);
//                if (member != null) {
//                    memberObj = member.get();
//                    model.addAttribute("successMessage", "User found !");
//                    model.addAttribute("member", memberObj);
//                    model.addAttribute("id", memberObj.getId());
//                } else {
//                    model.addAttribute("failureMessage", "Member not found");
//                }
//
//        } catch (Exception e) {
//             model.addAttribute("failureMessage", "Member not found: "+e.getMessage());
//        }
//        return "searchMember";
//    }



//    @PostMapping("/deleteMember")
//    public String deleteMember(@RequestParam("email") String email, Model model) {
//        try {
//            boolean isDeleted = memberService.deleteByEmail(email); // Call the service layer to delete
//            if (isDeleted) {
//                model.addAttribute("successMessage", "Member with email '" + email + "' has been deleted.");
//                logger.info("Deleted member with email: {}", email);
//            } else {
////                model.addAttribute("failureMessage", "No member found with email: " + email);
//                logger.warn("Unable to delete user with email -- ", email);
//            }
//        } catch (Exception e) {
//            logger.error("Error deleting member with email: {}", email, e);
//            model.addAttribute("failureMessage", "An error occurred while trying to delete the member.");
//        }
//        return "success"; // Redirect to the members list or any appropriate page
//    }

    /**
     * Update a member by email.
     */

    @PostMapping("/updateMember")
    public String updateMember(@RequestParam("id") String id,
                               @Valid @ModelAttribute("member") Member updatedMember,
                               BindingResult result,
                               Model model) {

        try {
            boolean isUpdated = memberService.updateById(id, updatedMember); // Call the service layer to update
            if (isUpdated) {
                model.addAttribute("successMessage", "Member with email '" + id + "' has been updated.");
                logger.info("Updated member with email: {}", id);
            } else {
//                model.addAttribute("failureMessage", "No member found with email: " + email);
                logger.warn("failed to update user with email : {}", id);
            }
        } catch (Exception e) {
            logger.error("Error updating member with email: {}", id, e);
            model.addAttribute("failureMessage", "An error occurred while trying to update the member.");
        }

        return "success"; // Redirect to the members list or any appropriate page
    }

    @GetMapping("/showUpdatePage")
    public String updateMember(@RequestParam("id") String id, Model model) {

       try{
           model.addAttribute("UpdateMember", "fORM TO UPDATE MEMBERS");
           Optional<Member> member  = memberService.findById(id);
           model.addAttribute("member",member.get());
       }
    catch (Exception ex){

    }
        return "updateMember"; // Redirect to the members list or any appropriate page
    }


    @GetMapping("/profile")
    public String getProfile(Model model,@RequestParam("id") String id) {
        // Fetch the authenticated user's details
        Member member = memberService.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for id " + id));

        // Add user details to the model
        model.addAttribute("member", member);
        return "profile";
    }
}
