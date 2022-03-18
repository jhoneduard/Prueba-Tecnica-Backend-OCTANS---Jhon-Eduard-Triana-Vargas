package com.backendapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backendapp.models.entity.User;

public interface IUserDao extends JpaRepository<User, Long> {

	@Query("select u from User u where u.name like %:name%")
	public List<User> usersFindByName(@Param("name") String name);

	public User findByName(String name);
}
