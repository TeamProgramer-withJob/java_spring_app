package com.example.its.domain.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.its.domain.entity.EntityUser;

@Mapper
public interface UserRepository {

    @Select("select * from users where username = #{username}")
    Optional<EntityUser> findByUsername(String username);

}