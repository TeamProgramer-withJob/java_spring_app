package com.example.its.domain.follow;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FollowRepository {

    @Insert("INSERT INTO follows (follower_id, followee_id) VALUES (#{followerId}, #{followeeId})")
    void insert(FollowEntity follow);

    @Delete("DELETE FROM follows WHERE follower_id = #{followerId} AND followee_id = #{followeeId}")
    void delete(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    @Select("SELECT COUNT(*) > 0 FROM follows WHERE follower_id = #{followerId} AND followee_id = #{followeeId}")
    boolean exists(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    /** 指定ユーザーがフォローしている人数 */
    @Select("SELECT COUNT(*) FROM follows WHERE follower_id = #{userId}")
    int countFollowing(@Param("userId") Long userId);

    /** 指定ユーザーをフォローしている人数（フォロワー数） */
    @Select("SELECT COUNT(*) FROM follows WHERE followee_id = #{userId}")
    int countFollowers(@Param("userId") Long userId);
}
