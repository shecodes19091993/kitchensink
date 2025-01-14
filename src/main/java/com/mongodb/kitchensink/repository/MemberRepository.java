package com.mongodb.kitchensink.repository;

import com.mongodb.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {

    @Override
    public Optional<Member> findById(String id) ;

    public Member findByEmail(String email);

    public List<Member> findAllOrderedByName();

    @Override
    public Member save(Member entity);

    Optional<Member> findByName(String username);
}
