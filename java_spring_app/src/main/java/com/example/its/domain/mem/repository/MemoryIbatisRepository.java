package com.example.its.domain.mem.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.its.domain.mem.entity.EntityMemory;

import java.util.List;

@Mapper
public interface MemoryIbatisRepository {

    @Select("select * from memories")
    List<EntityMemory> findAll();

    // jdbcType=VARCHAR VARCHAR must be upper-case, set jdbcType for nullable column. @Param annotation is recommended.
    @Insert("insert into memories (user_id, title, details) values (#{userId}, #{title}, #{details, jdbcType=VARCHAR})")
    void insert(@Param("userId") int userId,@Param("title") String title, @Param("details") String details);

    @Select("select * from memories where id = #{id}")
    EntityMemory findById(@Param("id") int id);
}
