package com.github.bevilacqua1996;

import com.github.bevilacqua1996.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class GenericTests {

    @Test
    public void UsuarioEqualsTest() {
        Usuario usuario1 = new Usuario("Usuário");
        Usuario usuario2 = new Usuario("Usuário");

        Assert.assertEquals("Erro de comparação", usuario1, usuario2);

        Assert.assertNotSame(usuario1, usuario2);

    }

}
