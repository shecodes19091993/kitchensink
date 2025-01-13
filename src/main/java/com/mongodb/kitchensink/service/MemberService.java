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

          memberRepository.save(newMember);
    }
}
