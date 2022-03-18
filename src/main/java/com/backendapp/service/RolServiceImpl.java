package com.backendapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backendapp.dao.IRolDao;
import com.backendapp.models.entity.Rol;

@Service
public class RolServiceImpl implements IRolService {
	@Autowired
	private IRolDao rolDao;

	@Override
	@Transactional(readOnly = true)
	public List<Rol> findAllRoles() {
		return rolDao.findAllRol();
	}
}
