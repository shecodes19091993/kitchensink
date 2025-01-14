package com.mongodb.kitchensink.service;

import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        Member member = new Member();
        member.setEmail(email);

        when(memberRepository.findByEmail(email)).thenReturn(member);

        Member result = memberService.findByEmail(email);
        assertNotNull(result);
        assertEquals(email, result.getEmail());

        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindById() {
        String id = "123";
        Member member = new Member();
        member.setId(id);

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        Optional<Member> result = memberService.findById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());

        verify(memberRepository, times(1)).findById(id);
    }

    @Test
    void testFindAllOrderedByName() {
        Member member1 = new Member();
        member1.setName("Alice");
        Member member2 = new Member();
        member2.setName("Bob");

        List<Member> members = Arrays.asList(member1, member2);

        when(memberRepository.findAll(Sort.by(Sort.Order.asc("name")))).thenReturn(members);

        List<Member> result = memberService.findAllOrderedByName();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());

        verify(memberRepository, times(1)).findAll(Sort.by(Sort.Order.asc("name")));
    }

    @Test
    void testRegister() throws Exception {
        Member newMember = new Member();
        newMember.setName("Test User");
        newMember.setPassword("password");

        when(memberRepository.save(any(Member.class))).thenReturn(newMember);

        memberService.register(newMember);

        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testDeleteByEmail() {
        String email = "test@example.com";
        Member member = new Member();
        member.setEmail(email);

        when(memberRepository.findByEmail(email)).thenReturn(member);
        doNothing().when(memberRepository).delete(member);

        boolean result = memberService.deleteByEmail(email);
        assertTrue(result);

        verify(memberRepository, times(1)).findByEmail(email);
        verify(memberRepository, times(1)).delete(member);
    }


    @Test
    void testUpdateById() {
        String id = "123";
        Member existingMember = new Member();
        existingMember.setId(id);
        existingMember.setName("Old Name");

        Member updatedMember = new Member();
        updatedMember.setName("New Name");
        updatedMember.setPhoneNumber("1234567890");
        updatedMember.setEmail("new@example.com");

        when(memberRepository.findById(id)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(existingMember)).thenReturn(existingMember);

        boolean result = memberService.updateById(id, updatedMember);
        assertTrue(result);
        assertEquals("New Name", existingMember.getName());
        assertEquals("1234567890", existingMember.getPhoneNumber());
        assertEquals("new@example.com", existingMember.getEmail());

        verify(memberRepository, times(1)).findById(id);
        verify(memberRepository, times(1)).save(existingMember);
    }

    @Test
    void testUpdateById_NotFound() {
        String id = "nonexistent";
        Member updatedMember = new Member();
        updatedMember.setName("New Name");

        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = memberService.updateById(id, updatedMember);
        assertFalse(result);

        verify(memberRepository, times(1)).findById(id);
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    void testFindByUsername() {
        String username = "testuser";
        Member member = new Member();
        member.setName(username);

        when(memberRepository.findByName(username)).thenReturn(Optional.of(member));

        Member result = memberService.findByUsername(username);
        assertNotNull(result);
        assertEquals(username, result.getName());

        verify(memberRepository, times(1)).findByName(username);
    }

    @Test
    void testFindByUsername_NotFound() {
        String username = "nonexistentuser";

        when(memberRepository.findByName(username)).thenReturn(Optional.empty());

        Member result = memberService.findByUsername(username);
        assertNull(result);

        verify(memberRepository, times(1)).findByName(username);
    }
}
