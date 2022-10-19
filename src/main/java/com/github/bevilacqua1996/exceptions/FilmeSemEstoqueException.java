package com.github.bevilacqua1996.exceptions;

public class FilmeSemEstoqueException extends Exception{
    public FilmeSemEstoqueException(String filme_sem_estoque) {
        super(filme_sem_estoque);
    }
}
