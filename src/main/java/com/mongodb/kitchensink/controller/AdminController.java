package com.mongodb.kitchensink.controller;

import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * AdminController handles administrative tasks such as member registration,
 * updating member details, and member management functionalities.
 */
@RequestMapping("admin")
@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private MemberService memberService;

    /**
     * Displays the registration form. If an "id" is provided, it fetches the corresponding member details.
     *
     * @param model the model to hold form data
     * @param id the ID of the member to edit (optional)
     * @return the registration form view name
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model, @RequestParam(value = "id", required = false) String id) {
        if (!StringUtils.isEmpty(id)) {
            Member admin = memberService.findById(id).orElse(null);
            model.addAttribute("admin", admin);
        }
        model.addAttribute("member", new Member());
        logger.info("Showing registration form to user.");
        return "register";
    }

    /**
     * Handles the registration of a new member.
     *
     * @param newMember the new member object to register
     * @param model the model to display success or failure messages
     * @return the registration result view name
     */
    @RequestMapping(value = "/registerMember", method = RequestMethod.POST)
    public String register(@Valid Member newMember, Model model) {
        try {
            memberService.register(newMember);
            model.addAttribute("successMessage", "Registration successful");
            model.addAttribute("Id", newMember.getId());
            model.addAttribute("Name", newMember.getName());
            model.addAttribute("Email", newMember.getEmail());
            model.addAttribute("PhoneNumber", newMember.getPhoneNumber());
            logger.info("Registering user with details: {}", newMember.getName());
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage());
            model.addAttribute("failureMessage", "Registration unsuccessful: Please retry again! " + e.getMessage());
        }
        return "membersRegistration";
    }

    /**
     * Retrieves a list of all members, ordered by their names.
     *
     * @param model the model to hold the list of members
     * @return the members list view name
     */
    @GetMapping("/members")
    public String getAllMembers(Model model) {
        List<Member> members = memberService.findAllOrderedByName();
        model.addAttribute("members", members);
        return "members";
    }

    /**
     * Searches for a member by their ID.
     *
     * @param id the ID of the member to search for
     * @param model the model to display member details or failure message
     * @return the member search result view name
     */
    @GetMapping("/search/members/")
    public String findById(@RequestParam(value = "id", required = false) String id, Model model) {
        try {
            Optional<Member> member = memberService.findById(id);
            if (member.isPresent()) {
                model.addAttribute("successMessage", "User found!");
                model.addAttribute("member", member.get());
            } else {
                model.addAttribute("failureMessage", "Member not found");
            }
        } catch (Exception e) {
            model.addAttribute("failureMessage", "Member not found: " + e.getMessage());
        }
        return "searchMember";
    }

    /**
     * Deletes a member by their email address.
     *
     * @param email the email address of the member to delete
     * @param model the model to display success or failure messages
     * @return the result view name
     */
    @PostMapping("/deleteMember")
    public String deleteMember(@RequestParam("email") String email, Model model) {
        try {
            boolean isDeleted = memberService.deleteByEmail(email);
            if (isDeleted) {
                model.addAttribute("successMessage", "Member with email '" + email + "' has been deleted.");
                logger.info("Deleted member with email: {}", email);
            } else {
                logger.warn("Unable to delete user with email: {}", email);
            }
        } catch (Exception e) {
            logger.error("Error deleting member with email: {}", email, e);
            model.addAttribute("failureMessage", "An error occurred while trying to delete the member.");
        }
        return "success";
    }

    /**
     * Updates a member's details using their ID.
     *
     * @param id the ID of the member to update
     * @param updatedMember the updated member details
     * @param result binding result for validation
     * @param model the model to display success or failure messages
     * @return the result view name
     */
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