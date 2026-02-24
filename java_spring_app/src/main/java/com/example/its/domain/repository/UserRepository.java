package com.example.its.domain.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.its.domain.entity.EntityUser;

@Mapper
public interface UserRepository {

    @Select("select * from users where email = #{email}")
    Optional<EntityUser> findByEmail(String email);

    @Select("select * from users where id = #{id}")
    Optional<EntityUser> findById(int id);

    @Select("select count(id) from users where email = #{email}")
    int checkEmailUnique(String email);
    
    @Insert("insert into users(name,password,email,role) values(#{name},#{password},#{email},#{role})")
    void save(@Param("name") String name, @Param("password") String password, @Param("email") String email, @Param("role") String role);
}