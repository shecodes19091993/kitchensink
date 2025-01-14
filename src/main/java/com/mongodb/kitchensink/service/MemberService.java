package com.mongodb.kitchensink.service;

import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MemberService
{
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    @Autowired
    private MemberRepository memberRepository;

    /**
     * Finds a member by their email.
     *
     * @param email the email of the member to find
     * @return the member with the given email, or null if no such member exists
     */
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /**
     * Finds a member by their ID.
     *
     * @param id the ID of the member to find
     * @return an {@link Optional} containing the member if found, or empty if no such member exists
     */
    public Optional<Member> findById(String id){
        return memberRepository.findById(id);
    }

    /**
     * Finds all members, ordered by their name in ascending order.
     *
     * @return a list of all members, sorted by name
     */
    public List<Member> findAllOrderedByName() {
        // Use MongoTemplate for custom queries and sorting
        return memberRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    /**
     * Registers a new member by saving them in the database.
     * The member's password is encoded before saving.
     *
     * @param newMember the member to register
     * @throws Exception if an error occurs during registration
     */
    public void register(Member newMember) throws Exception {
        logger.info("Registering in db " + newMember.getName());
        newMember.setPassword(newMember.passwordEncoder().encode(newMember.getPassword()));
          memberRepository.save(newMember);
    }

    /**
     * Deletes a member by their email.
     *
     * @param email the email of the member to delete
     * @return {@code true} if the member was successfully deleted, {@code false} otherwise
     */
    public boolean deleteByEmail(String email) {
        Optional<Member> member = Optional.ofNullable(memberRepository.findByEmail(email));
        if (member.isPresent()) {
            logger.info("Deleting user in db " + member.get().getName());
            memberRepository.delete(member.get());
            return true;
        }
        logger.error("Failed Deleting user in db " + member.get().getName()+"-- email"+email);
        return false;
    }

    /**
     * Updates a member by their ID with the provided updated member information.
     *
     * @param id the ID of the member to update
     * @param updatedMember the updated member information
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    public boolean updateById(String id, Member updatedMember) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member existingMember = memberOptional.get();
            existingMember.setName(updatedMember.getName());
            existingMember.setPhoneNumber(updatedMember.getPhoneNumber());
            existingMember.setEmail(updatedMember.getEmail());
            // Add more fields as necessary
            logger.info("Updating user in db " + existingMember.getName());
            memberRepository.save(existingMember);
            return true;
        }
        logger.error("Failed updating user in db " + updatedMember.getName());
        return false;
    }

    /**
     * Finds a member by their username.
     *
     * @param username the username of the member to find
     * @return the member with the given username, or null if no such member exists
     */
    public Member findByUsername(String username) {
        return memberRepository.findByName(username).stream().findFirst().orElse(null);
    }
}
