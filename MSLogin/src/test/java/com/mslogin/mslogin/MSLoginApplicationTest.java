package com.mslogin.mslogin;


import com.ms.common.DTO.BaseDTO;
import com.mslogin.mslogin.Controller.UserController;
import com.mslogin.mslogin.DTO.DTOCadastrarUsuario;
import com.mslogin.mslogin.DTO.DTOLogarUsuario;
import com.mslogin.mslogin.Model.User;
import com.mslogin.mslogin.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class MSloginApplicationTests {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserController controller;

	@Test
	public void testCadastrarUsuario_Success() {
		
		DTOCadastrarUsuario dto = new DTOCadastrarUsuario("3342", "13234", "1332423");


		when(userRepository.findByUser(anyString(), anyString())).thenReturn(null);

		BaseDTO result = controller.cadastrarUsuario(dto);

		assertTrue(result.sucess());
		assertEquals("Usuário cadastrado com sucesso.", result.response().toString());
	}

	@Test
	public void testCadastrarUsuario_UserAlreadyExists() {
		
		DTOCadastrarUsuario dto = new DTOCadastrarUsuario("3342", "13234", "1332423");

		// Simulating that a user already exists
		when(userRepository.findByUser(anyString(), anyString())).thenReturn(new User());

		BaseDTO result = controller.cadastrarUsuario(dto);

		assertFalse(result.sucess());
		assertEquals("Usuário já existente.", result.response());
	}


	@Test
	public void testLogarUsuario_UserNotFound() {
		
		DTOLogarUsuario dto = new DTOLogarUsuario("username", "password");

		// Simulating that the user is not found
		when(userRepository.findByUser(anyString(), anyString())).thenReturn(null);

		BaseDTO result = controller.logarUsuario(dto);

		assertFalse(result.sucess());
		assertEquals("Usuário não encontrado.", result.response());
	}
	@Test
	public void testLogarUsuario_Success() {
		
		DTOLogarUsuario dto = new DTOLogarUsuario("username", "password");

		// Simulating that the user is found
		User user = new User();
		when(userRepository.findByUser(anyString(), anyString())).thenReturn(user);

		BaseDTO result = controller.logarUsuario(dto);

		assertTrue(result.sucess());
		assertEquals("Usuário logado com sucesso.", result.response());
	}

}


