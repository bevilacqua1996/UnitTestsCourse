package com.github.bevilacqua1996.servicos;

import com.github.bevilacqua1996.entidades.Usuario;
import com.github.bevilacqua1996.exceptions.LocadoraException;

public interface SPCService {

    boolean possuiNegativacao(Usuario usuario) throws Exception;

}
