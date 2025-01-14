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

import java.util.Optional;


@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private MemberService memberService;

    /**
     * Test endpoint for verifying user controller functionality.
     *
     * @return a test message
     */
    @GetMapping("/test")
    @ResponseBody
    public String testEndpoint() {
        logger.info("Test endpoint accessed.");
        return "This is the test endpoint!";
    }

    /**
     * Displays the profile of a member based on their ID.
     *
     * @param model the model to hold member details
     * @param id the ID of the member whose profile is requested
     * @return the profile view name
     */
    @GetMapping("/profile")
    public String getProfile(Model model, @RequestParam("id") String id) {
        Member member = memberService.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for id " + id));
        model.addAttribute("member", member);
        return "profile";
    }

    @PostMapping("/updateMember")
    public String updateMember(@RequestParam("id") String id,
                               @Valid @ModelAttribute("member") Member updatedMember,
                               BindingResult result,
                               Model model) {
        try {
            boolean isUpdated = memberService.updateById(id, updatedMember);
            if (isUpdated) {
                model.addAttribute("successMessage", "Member with ID '" + id + "' has been updated.");
                logger.info("Updated member with ID: {}", id);
            } else {
                logger.warn("Failed to update member with ID: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error updating member with ID: {}", id, e);
            model.addAttribute("failureMessage", "An error occurred while trying to update the member.");
        }
        return "success";
    }

    /**
     * Displays the form to update a member's details.
     *
     * @param id the ID of the member to update
     * @param model the model to hold member details
     * @return the update form view name
     */
    @GetMapping("/showUpdatePage")
    public String updateMember(@RequestParam("id") String id, Model model) {
        try {
            Optional<Member> member = memberService.findById(id);
            model.addAttribute("member", member.orElse(null));
        } catch (Exception e) {
            logger.error("Error fetching member for update: {}", id, e);
        }
        return "updateMember";
    }
}
