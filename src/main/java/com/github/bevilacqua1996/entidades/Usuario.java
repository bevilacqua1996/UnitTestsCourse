package com.github.bevilacqua1996.entidades;

public class Usuario {

	private String nome;
	
	public Usuario() {}
	
	public Usuario(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object usuario) {
		if (this == usuario) return true;
		if (usuario == null || getClass() != usuario.getClass()) return false;
		Usuario usuarioInstance = (Usuario) usuario;
		return nome.equals((usuarioInstance.getNome()));
	}

	@Override
	public String toString() {
		return "Usuario [name=" + nome + "]";
	}
}