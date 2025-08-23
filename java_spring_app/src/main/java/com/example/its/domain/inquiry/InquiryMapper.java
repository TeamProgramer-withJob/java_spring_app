package com.example.its.domain.inquiry;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface InquiryMapper {

    @Insert("""
        INSERT INTO inquiry(name, email, subject, message)
        VALUES(#{name}, #{email}, #{subject}, #{message})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Inquiry inquiry);

    @Select("SELECT id, name, email, subject, message, created_at AS createdAt FROM inquiry ORDER BY id DESC")
    List<Inquiry> findAll();
}
