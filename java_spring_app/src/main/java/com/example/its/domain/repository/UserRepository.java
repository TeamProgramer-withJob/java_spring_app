package com.example.its.domain.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
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
    
    @Select("select * from users order by id asc limit #{size} offset #{offset}")
    List<EntityUser> findAll(@Param("size") int size, @Param("offset") int offset);

    @Select("select count(id) from users")
    int totalCount();

    @Insert("update users set name=#{name}, role=#{role}, valid=#{valid}, force_pwd_change=#{forcePwdChg} where id=#{id}")
    void update(@Param("id") int id, @Param("name") String name, @Param("role") String role, @Param("valid") boolean valid, @Param("forcePwdChg") boolean forcePwdChg);

    @Insert("update users set name=#{name} where id=#{id}")
    void updateUserName(@Param("id") int id, @Param("name") String name);

    @Insert("update users set password=#{pwd} where id=#{id}")
    void updatePwd(@Param("id") int id, @Param("pwd") String pwd);

    @Delete("delete from users where id=#{id}")
    void delete(@Param("id") int id);

    @Delete("delete from users where email=#{email}")
    void deleteByEmail(@Param("email") String email);

    @Insert("insert into users(name,password,email,role) values(#{name},#{password},#{email},#{role})")
    void save(@Param("name") String name, @Param("password") String password, @Param("email") String email, @Param("role") String role);
}