package com.mongodb.kitchensink.controller;

import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testTestEndpoint() throws Exception {
        // Perform GET request to /user/test endpoint
        mockMvc.perform(get("/user/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("This is the test endpoint!"));
    }

    @Test
    void testUpdateMember() throws Exception {
        // Mock member data
        Member updatedMember = new Member();
        updatedMember.setId("123");
        updatedMember.setName("Updated Name");

        // Correctly mock a method that returns boolean
        when(memberService.updateById("123", updatedMember)).thenReturn(true);

        mockMvc.perform(post("/user/updateMember")
                        .param("id", "123")
                        .flashAttr("member", updatedMember))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attribute("successMessage", "Member with email '123' has been updated."));
    }

    @Test
    void testShowUpdatePage() throws Exception {
        // Mock member data
        Member member = new Member();
        member.setId("123");
        member.setName("Alice");

        when(memberService.findById("123")).thenReturn(java.util.Optional.of(member));

        mockMvc.perform(get("/user/showUpdatePage")
                        .param("id", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateMember"))
                .andExpect(model().attribute("member", member))
                .andExpect(model().attribute("UpdateMember", "fORM TO UPDATE MEMBERS"));
    }




}
