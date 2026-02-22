package com.example.its.domain.auth;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository {

    @Select("SELECT * FROM users WHERE username = #{username}")
    Optional<User> findByUsername(String username);

    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    boolean existsByUsername(String username);

    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    boolean existsByEmail(String email);

    @Insert("INSERT INTO users (username, display_name, email, password, bio, role) " +
            "VALUES (#{username}, #{displayName}, #{email}, #{password}, #{bio}, 'USER')")
    void insert(User user);
}