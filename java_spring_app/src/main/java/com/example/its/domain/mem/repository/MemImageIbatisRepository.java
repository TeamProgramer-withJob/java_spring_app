package com.example.its.domain.mem.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.its.domain.mem.entity.EntityMemImage;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemImageIbatisRepository {

    @Select("select id from mem_images where mem_id = #{id} order by created_at")
    List<EntityMemImage> findByMemId(@Param("id") int id);

    @Insert("insert into mem_images (mem_id, filename, content_type, image) values (#{memId}, #{filename}, #{contentType}, #{image})")
    void save(@Param("memId") int memId, @Param("filename") String filename, @Param("contentType") String contentType, @Param("image") byte[] image);

    @Select("select * from mem_images where id = #{id}")
    Optional<EntityMemImage> findById(@Param("id") int id);

    @Delete("delete from mem_images where id = #{id}")
    boolean delete(@Param("id") int id);
}
