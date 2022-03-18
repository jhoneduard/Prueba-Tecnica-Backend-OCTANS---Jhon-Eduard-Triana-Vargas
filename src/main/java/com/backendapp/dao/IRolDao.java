package com.backendapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backendapp.models.entity.Rol;

public interface IRolDao extends JpaRepository<Rol, Long> {

	@Query("from Rol")
	public List<Rol> findAllRol();
}