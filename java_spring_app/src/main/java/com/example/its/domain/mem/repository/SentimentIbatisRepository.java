package com.example.its.domain.mem.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.its.domain.mem.entity.EntitySentiment;

import java.util.List;

@Mapper
public interface SentimentIbatisRepository {

    @Select("select * from sentiments where mem_id = #{id} order by created_at")
    List<EntitySentiment> findByMemId(@Param("id") int id);

    @Insert("insert into sentiments (user_id, mem_id, sentiment) values (#{userId}, #{memId}, #{sentiment})")
    void save(@Param("userId") int userId, @Param("memId") int memId, @Param("sentiment") int sentiment);
}
