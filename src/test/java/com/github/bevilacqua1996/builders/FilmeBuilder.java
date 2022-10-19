package com.github.bevilacqua1996.builders;

import com.github.bevilacqua1996.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    public FilmeBuilder() {

    }

    public static FilmeBuilder umFilme() {
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme = new Filme();
        filmeBuilder.filme.setEstoque(3);
        filmeBuilder.filme.setNome("Filme Aleatório");
        filmeBuilder.filme.setPrecoLocacao(5.0);
        return filmeBuilder;
    }

    public static FilmeBuilder umFilmeSemEstoque() {
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme = new Filme();
        filmeBuilder.filme.setEstoque(0);
        filmeBuilder.filme.setNome("Filme Aleatório");
        filmeBuilder.filme.setPrecoLocacao(5.0);
        return filmeBuilder;
    }

    public FilmeBuilder comValor(Double valor) {
        filme.setPrecoLocacao(10.0);
        return this;
    }

    public Filme agora() {
        return filme;
    }

}
