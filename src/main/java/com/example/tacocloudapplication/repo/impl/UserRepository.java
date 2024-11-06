package com.example.tacocloudapplication.repo.impl;

import com.example.tacocloudapplication.table.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "update app_user set username = :newUsername where username = :oldUsername", nativeQuery = true)
    int updateUsername(@Param("oldUsername") String oldUsername, @Param("newUsername") String newUsername);

    @Transactional
    @Modifying
    @Query(value = "update app_user set password = :newPassword where username = :currentUsername", nativeQuery = true)
    int updatePassword(@Param("currentUsername") String currentUsername, @Param("newPassword") String newPassword);

    @Transactional
    @Query(value = "select password from app_user where username = :currentUsername", nativeQuery = true)
    String getCurrentPassword(@Param("currentUsername") String currentUsername);

    @Transactional
    @Query(value = "select * from app_user where username = :username", nativeQuery = true)
    User getUserByUsername(@Param("username") String username);

    @Transactional
    @Query(value = "select id from app_user where username = :username", nativeQuery = true)
    Integer getUserIdByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value = "delete from app_user where username = :username", nativeQuery = true)
    Integer deleteUserByUsername(@Param("username") String username);

}

