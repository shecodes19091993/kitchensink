package com.mongodb.kitchensink.service;

import com.mongodb.kitchensink.controller.AdminController;
import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomMemberDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomMemberDetailsService.class);


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from MongoDB
      try {
          Member member = userRepository.findByName(username)
                  .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

          // Build UserDetails object
          return org.springframework.security.core.userdetails.User.builder()
                  .username(member.getName() + "-" + member.getId())
                  .password(member.getPassword()) // Password must already be encoded in DB
                  .roles(member.getRole()) // Roles are stored as strings in MongoDB
                  .build();
      }
      catch(Exception e){
          logger.info("Exception while user logging in");
          return null;
      }
    }
}