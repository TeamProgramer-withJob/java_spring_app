package com.example.its.domain.information;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InformationRepository {

    @Select("select * from informations order by created_at desc")
    List<InformationEntity> findAll();

    @Select("select * from informations where id = #{id}")
    Optional<InformationEntity> findById(long id);

    @Insert("insert into informations (information_title, information_detail) values (#{informationTitle}, #{informationDetail})")
    void insert(String informationTitle, String informationDetail);

    @Update("update informations set information_title = #{informationTitle}, information_detail = #{informationDetail} where id = #{id}")
    void update(long id, String informationTitle, String informationDetail);

    @Delete("delete from informations where id = #{id}")
    void delete(long id);
}
