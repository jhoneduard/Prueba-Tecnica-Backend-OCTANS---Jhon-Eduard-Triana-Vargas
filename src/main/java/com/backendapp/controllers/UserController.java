package com.backendapp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendapp.models.entity.User;
import com.backendapp.service.IUserService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private IUserService userService;

	@GetMapping("/users")
	public List<User> index() {
		return userService.findAll();
	}

	@GetMapping("/users/page/{page}")
	public Page<User> index(@PathVariable Integer page) {
		int numberOfRecordsPage = 4;
		Pageable pageable = PageRequest.of(page, numberOfRecordsPage);
		return userService.findAll(pageable);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<?> show(@PathVariable long id) {

		User user = null;
		Map<String, Object> response = new HashMap<>();
		try {
			user = userService.findById(id);

		} catch (DataAccessException ex) {
			response.put("mensaje", "Errro al realizar la consulta a la bd");
			response.put("error", ex.getCause() + " :" + ex.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (user == null) {
			response.put("mensaje",
					"El usuario : ".concat(String.valueOf(id).concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
		User userNew = null;
		Map<String, Object> response = new HashMap<>();
		List<String> errors = new ArrayList<>();

		if (result.hasErrors()) {
			for (FieldError err : result.getFieldErrors()) {
				errors.add(err.getDefaultMessage());
			}
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			User userTem = userService.getUserByName(user.getName());
			if (userTem != null) {
				errors.add("El nombre ya existe en la base de datos...");
				response.put("errors", errors);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			} else {
				userNew = userService.save(user);
			}

		} catch (DataAccessException ex) {
			response.put("mensaje", "Errro al realizar el insert en la base de datos");
			response.put("error", ex.getCause() + " :" + ex.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El usuario ha sido creado con exito!!");
		response.put("user", userNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id) {
		User userActually = userService.findById(id);
		User userUpdated = null;
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();
			for (FieldError err : result.getFieldErrors()) {
				errors.add(err.getDefaultMessage());
			}
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (userActually == null) {
			response.put("mensaje", "Error:  no se pudo editar, El usuario : "
					.concat(String.valueOf(id).concat(" No existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			userActually.setName(user.getName());
			userActually.setRol(user.getRol());
			userActually.setActive(user.getActive());

			User userTem = userService.getUserByName(user.getName());

			if (userTem != null && userTem.getName() != user.getName()) {
				List<String> errors = new ArrayList<>();
				errors.add("El nombre ya existe en la base de datos...");
				response.put("errors", errors);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			}

			userUpdated = userService.save(userActually);
		} catch (DataAccessException ex) {
			response.put("mensaje", "Errro al realizar al actualizar el usuario en la base de datos");
			response.put("error", ex.getCause() + " :" + ex.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El usuario ha sido actualizado con exito!!");
		response.put("user", userUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			userService.delete(id);
		} catch (DataAccessException ex) {
			response.put("mensaje", "Errro al realizar la  eliminacion del usuario en la base de datos");
			response.put("error", ex.getCause() + " :" + ex.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El usuario ha sido eliminado con exito!!!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/users/filter/{name}")
	public List<User> getUserByName(@PathVariable String name) {
		return userService.getUsersByName(name);
	}

}