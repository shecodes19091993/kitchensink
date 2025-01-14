package com.mongodb.kitchensink.service;

import com.mongodb.kitchensink.controller.AdminController;
import com.mongodb.kitchensink.model.Member;
import com.mongodb.kitchensink.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomMemberDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomMemberDetailsService.class);

    /**
     * Loads a user by their username for authentication.
     *
     * This method retrieves the user details from the database using the username,
     * and converts it into a {@link org.springframework.security.core.userdetails.UserDetails} object
     * for Spring Security authentication. If the user is not found, a {@link UsernameNotFoundException}
     * is thrown.
     *
     * @param username the username of the user to be loaded
     * @return a {@link org.springframework.security.core.userdetails.UserDetails} object containing
     *         the user's authentication information
     * @throws UsernameNotFoundException if the user with the given username is not found
     * @throws BadCredentialsException if invalid credentials are provided
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, BadCredentialsException {

          Member member = userRepository.findByName(username)
                  .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

          // Build UserDetails object
          return org.springframework.security.core.userdetails.User.builder()
                  .username(member.getName() + "-" + member.getId())
                  .password(member.getPassword())
                  .roles(member.getRole())
                  .build();

    }
}