package com.example.its.domain.memory;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface MemoryRepository {

    /** 一覧表示用：image_data（BLOB）は取得しない */
    @Select("""
            SELECT m.id, m.cemetery_id, m.author_id,
                   u.display_name AS author_display_name,
                   m.title, m.body, m.image_content_type, m.visibility, m.created_at
            FROM memories m
            JOIN users u ON m.author_id = u.id
            WHERE m.cemetery_id = #{cemeteryId}
            ORDER BY m.created_at DESC
            """)
    List<MemoryEntity> findByCemeteryId(Long cemeteryId);

    /** 詳細表示用：image_data（BLOB）は取得しない */
    @Select("""
            SELECT m.id, m.cemetery_id, m.author_id,
                   u.display_name AS author_display_name,
                   m.title, m.body, m.image_content_type, m.visibility, m.created_at
            FROM memories m
            JOIN users u ON m.author_id = u.id
            WHERE m.id = #{id}
            """)
    Optional<MemoryEntity> findById(Long id);

    /** 画像バイナリ取得専用（コントローラーの画像配信エンドポイント用）
     *  jdbcType=VARBINARY を指定して SQLite JDBC の getBytes() を使わせる */
    @Results({
        @Result(property = "imageData",        column = "image_data",         jdbcType = JdbcType.VARBINARY),
        @Result(property = "imageContentType", column = "image_content_type", jdbcType = JdbcType.VARCHAR)
    })
    @Select("SELECT image_data, image_content_type FROM memories WHERE id = #{id}")
    Optional<MemoryEntity> findImageById(Long id);

    @Insert("""
            INSERT INTO memories (cemetery_id, author_id, title, body, image_data, image_content_type, visibility)
            VALUES (#{cemeteryId}, #{authorId}, #{title}, #{body}, #{imageData,jdbcType=VARBINARY}, #{imageContentType}, #{visibility})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(MemoryEntity memory);

    /** サンプルデータ投入用：image_data が未設定の場合のみ画像を更新する */
    @Update("UPDATE memories SET image_data = #{imageData,jdbcType=VARBINARY}, image_content_type = #{contentType} WHERE id = #{id} AND image_data IS NULL")
    void updateImageIfAbsent(@Param("id") Long id, @Param("imageData") byte[] imageData, @Param("contentType") String contentType);
}
