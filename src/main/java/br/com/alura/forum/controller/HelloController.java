package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	
	@RequestMapping("/")
	@ResponseBody // diz ao Spring que não deve procurar por uma página JSF por exemplo pra retornar... o retorno é a String Hello world mesmo
	public String hello() {
		return "Hello world";
	}
}
