package com.github.bevilacqua1996.daos;

import com.github.bevilacqua1996.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {

    void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();
}
