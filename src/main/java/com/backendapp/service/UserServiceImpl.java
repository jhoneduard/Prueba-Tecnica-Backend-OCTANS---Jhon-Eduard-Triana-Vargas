package com.backendapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendapp.dao.IUserDao;
import com.backendapp.models.entity.User;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return (List<User>) userDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return userDao.findAll(pageable);
	}

	@Override
	@Transactional
	public User save(User user) {
		return userDao.save(user);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		userDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public User findById(long id) {
		return userDao.findById(id).orElse(null);
	}

	@Override
	public List<User> getUsersByName(String name) {
		return (List<User>) userDao.usersFindByName(name);
	}

	@Override
	public User getUserByName(String name) {
		return userDao.findByName(name);
	}

}
