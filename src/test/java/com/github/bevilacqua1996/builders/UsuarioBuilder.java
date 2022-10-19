package com.github.bevilacqua1996.builders;

import com.github.bevilacqua1996.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    public UsuarioBuilder(){

    }

    public static UsuarioBuilder umUsuario() {
        UsuarioBuilder usuarioBuilder = new UsuarioBuilder();
        usuarioBuilder.usuario = new Usuario();
        usuarioBuilder.usuario.setNome("Usuario 1");

        return usuarioBuilder;
    }

    public UsuarioBuilder comNome(String nome) {
        usuario.setNome(nome);
        return this;
    }

    public Usuario agora() {
        return usuario;
    }

}
