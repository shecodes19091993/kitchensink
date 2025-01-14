package com.mongodb.kitchensink.service;


import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomMemberDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CustomMemberDetailsService customMemberDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        String username = "testuser";
        Member member = new Member();
        member.setName(username);
        member.setId("123");
        member.setPassword("encoded_password");
        member.setRole("USER");

        when(memberRepository.findByName(username)).thenReturn(Optional.of(member));

        // Act
        UserDetails userDetails = customMemberDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser-123", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(memberRepository, times(1)).findByName(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";

        when(memberRepository.findByName(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () ->
                customMemberDetailsService.loadUserByUsername(username));

        verify(memberRepository, times(1)).findByName(username);
    }
}

