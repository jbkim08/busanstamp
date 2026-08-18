package com.example.busanstamp.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    //이메일로 가입된 유저 있는지 확인 ( 1: 유저있음 , 0: 유저없음,가입가능 )
    int existsByEmail(@Param("email") String email);
    //새 유저 저장
    int save(User user);
    //이메일로 유저찾아서 유저객체 리턴
    User findByEmail(@Param("email") String email);
    //id로 유저찾아서 유저객체 리턴
    User findById(@Param("userId") Long userId);
}