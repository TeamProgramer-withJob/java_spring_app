package com.example.its.domain.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.its.domain.entity.EntityInquiry;
import com.example.its.domain.entity.EntityUser;

@Mapper
public interface InquiryRepoMapper {

    @Select("select q.*, (u.email is not null) as email_exist from inquiries q left join users u on q.email=u.email order by q.id desc limit #{size} offset #{offset}")
    List<EntityInquiry> findAllWithUser(@Param("size") int size, @Param("offset") int offset);

    @Select("select count(id) from inquiries")
    int totalCount();
}