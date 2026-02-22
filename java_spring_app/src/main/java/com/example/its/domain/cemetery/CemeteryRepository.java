package com.example.its.domain.cemetery;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CemeteryRepository {

    @Select("""
            SELECT c.*, u.display_name AS owner_display_name
            FROM cemeteries c
            JOIN users u ON c.owner_id = u.id
            ORDER BY c.created_at DESC
            """)
    List<CemeteryEntity> findAll();

    @Select("""
            SELECT c.*, u.display_name AS owner_display_name
            FROM cemeteries c
            JOIN users u ON c.owner_id = u.id
            WHERE c.id = #{id}
            """)
    Optional<CemeteryEntity> findById(Long id);

    /** オーナーIDで霊園一覧を取得する（マイページ用） */
    @Select("""
            SELECT c.*, u.display_name AS owner_display_name
            FROM cemeteries c
            JOIN users u ON c.owner_id = u.id
            WHERE c.owner_id = #{ownerId}
            ORDER BY c.created_at DESC
            """)
    List<CemeteryEntity> findByOwnerId(Long ownerId);

    @Insert("INSERT INTO cemeteries (owner_id, name, description) VALUES (#{ownerId}, #{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CemeteryEntity cemetery);
}
