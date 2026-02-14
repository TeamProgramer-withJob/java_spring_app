package com.example.its.domain.inquiry;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InquiryRepository {

    @Select("select * from inquiries")
    List<InquiryEntity> findAll();

    @Insert("INSERT INTO inquiries(name, email, subject, message) VALUES(#{name}, #{email}, #{subject}, #{message})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(InquiryEntity inquiry);

    @Select("SELECT * from inquiries where id = #{inquiryId}")
    InquiryEntity findById(long inquiryId);
}

