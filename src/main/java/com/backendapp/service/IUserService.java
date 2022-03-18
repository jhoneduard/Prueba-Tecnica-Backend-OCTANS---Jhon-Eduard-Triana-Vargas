package com.backendapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backendapp.models.entity.User;

public interface IUserService {

	public List<User> findAll();

	public Page<User> findAll(Pageable pageable);

	public User save(User user);

	public void delete(Long id);

	public User findById(long id);

	public List<User> getUsersByName(String name);

	public User getUserByName(String name);
}