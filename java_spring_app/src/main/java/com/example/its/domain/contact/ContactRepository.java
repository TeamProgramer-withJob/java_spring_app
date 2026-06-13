package com.example.its.domain.contact;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ContactRepository {

    @Select("select * from contacts order by created_at desc")
    List<ContactEntity> findAll();

    @Select("select * from contacts where id = #{id}")
    Optional<ContactEntity> findById(long id);

    @Insert("insert into contacts (name, email, subject, message) values (#{name}, #{email}, #{subject}, #{message})")
    void insert(String name, String email, String subject, String message);

    @Delete("delete from contacts where id = #{id}")
    void delete(long id);
}
