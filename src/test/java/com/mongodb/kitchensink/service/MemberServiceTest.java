package com.mongodb.kitchensink.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setup() {
        member = new Member();
        member.setId("1");
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("1234567890");
    }

    @Test
    public void testFindByEmail_Success() {
        // Mock the repository call to return a member
        when(memberRepository.findByEmail("john.doe@example.com")).thenReturn(member);

        // Call the service method
        Member result = memberService.findByEmail("john.doe@example.com");

        // Validate the result
        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());

        // Verify the interaction with the mock repository
        verify(memberRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    public void testFindById_Success() {
        // Mock the repository to return a member for a given ID
        when(memberRepository.findById("1")).thenReturn(Optional.of(member));

        // Call the service method
        Optional<Member> result = memberService.findById("1");

        // Validate the result
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());

        // Verify the interaction with the mock repository
        verify(memberRepository, times(1)).findById("1");
    }

    @Test
    public void testFindById_Failure() {
        // Mock the repository to return an empty Optional
        when(memberRepository.findById("1")).thenReturn(Optional.empty());

        // Call the service method
        Optional<Member> result = memberService.findById("1");

        // Validate that the result is empty
        assertFalse(result.isPresent());

        // Verify the interaction with the mock repository
        verify(memberRepository, times(1)).findById("1");
    }

    @Test
    public void testFindAllOrderedByName() {
        // Create a list of members to be returned by the mock repository
        List<Member> members = List.of(member);

        // Mock the repository to return the list sorted by name
        when(memberRepository.findAll(Sort.by(Sort.Order.asc("name")))).thenReturn(members);

        // Call the service method
        List<Member> result = memberService.findAllOrderedByName();

        // Validate the result
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("John Doe", result.get(0).getName());

        // Verify the interaction with the mock repository
        verify(memberRepository, times(1)).findAll(Sort.by(Sort.Order.asc("name")));
    }

    @Test
    public void testRegister() throws Exception {
        // Call the service method
        memberService.register(member);

        // Verify that the save method was called once
        verify(memberRepository, times(1)).save(member);
    }
}
