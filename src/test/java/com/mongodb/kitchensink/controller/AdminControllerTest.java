package com.mongodb.kitchensink.controller;



import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class AdminControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }



    @Test
    void testRegisterMember() throws Exception {
        // Mocking the behavior of the service layer
        Member newMember = new Member();
        newMember.setName("John Doe");
        newMember.setEmail("john.doe@example.com");
        newMember.setPhoneNumber("987654310");
        newMember.setRole("user");
        newMember.setPassword("abc");

        doNothing().when(memberService).register(any(Member.class));

        mockMvc.perform(post("/admin/registerMember")
                        .flashAttr("member", newMember))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void testFindById() throws Exception {
        // Mocking the behavior of the service layer
        Member member = new Member();
        member.setId("123");
        when(memberService.findById("123")).thenReturn(java.util.Optional.of(member));

        mockMvc.perform(get("/admin/search/members/")
                        .param("id", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("searchMember"))
                .andExpect(model().attribute("successMessage", "User found!"))
                .andExpect(model().attribute("member", member));
    }

    @Test
    void testDeleteMember() throws Exception {
        // Mocking the behavior of the service layer
        when(memberService.deleteByEmail("john.doe@example.com")).thenReturn(true);

        mockMvc.perform(post("/admin/deleteMember")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attribute("successMessage", "Member with email 'john.doe@example.com' has been deleted."));
    }

    @Test
    void testUpdateMember() throws Exception {
        // Mocking the behavior of the service layer
        Member updatedMember = new Member();
        updatedMember.setName("Updated Name");
        when(memberService.updateById("123", updatedMember)).thenReturn(true);

        mockMvc.perform(post("/admin/updateMember")
                        .param("id", "123")
                        .flashAttr("member", updatedMember))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attribute("successMessage", "Member with ID '123' has been updated."));
    }

    @Test
    void testShowUpdatePage() throws Exception {
        // Mocking the behavior of the service layer
        Member member = new Member();
        member.setId("123");
        member.setName("Alice");

        when(memberService.findById("123")).thenReturn(java.util.Optional.of(member));

        mockMvc.perform(get("/admin/showUpdatePage")
                        .param("id", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateMember"))
                .andExpect(model().attribute("member", member))
                .andExpect(model().attribute("UpdateMember", "fORM TO UPDATE MEMBERS"));
    }
}