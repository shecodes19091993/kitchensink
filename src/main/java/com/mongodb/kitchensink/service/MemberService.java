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

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    public Optional<Member> findById(String id){
        return memberRepository.findById(id);
    }

    public List<Member> findAllOrderedByName() {
        // Use MongoTemplate for custom queries and sorting
        return memberRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }


    public void register(Member newMember) throws Exception {
        logger.info("Registering in db " + newMember.getName());
        newMember.setPassword(newMember.passwordEncoder().encode(newMember.getPassword()));
          memberRepository.save(newMember);
    }

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

    public Member findByUsername(String username) {
        return memberRepository.findByName(username).stream().findFirst().orElse(null);
    }
}
