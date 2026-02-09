package com.example.its.domain.issue.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.its.domain.issue.model.Issue2Form;

import java.util.List;

@Mapper
public interface IssueRepository {

    @Select("select id,summary,description,fullpath,media_type as mediaType from issues")
    List<Issue2Form> findAll();

    // jdbcType=VARCHAR VARCHAR must be upper-case, set jdbcType for nullable column. @Param annotation is recommended.
    @Insert("insert into issues (summary, description, fullpath, media_type) values (#{summary}, #{description}, #{fullpath, jdbcType=VARCHAR}, #{mediaType})")
    void insert(@Param("summary") String summary, @Param("description") String description, @Param("fullpath") String fullpath, @Param("mediaType") int mediaType);

    @Select("select id,summary,description,fullpath,media_type as mediaType from issues where id = #{issueId}")
    Issue2Form findById(long issueId);
}
