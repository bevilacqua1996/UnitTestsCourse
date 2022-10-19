package com.github.bevilacqua1996.servicos;

import com.github.bevilacqua1996.builders.FilmeBuilder;
import com.github.bevilacqua1996.builders.UsuarioBuilder;
import com.github.bevilacqua1996.daos.LocacaoDAO;
import com.github.bevilacqua1996.entidades.Filme;
import com.github.bevilacqua1996.entidades.Locacao;
import com.github.bevilacqua1996.entidades.Usuario;
import com.github.bevilacqua1996.exceptions.FilmeSemEstoqueException;
import com.github.bevilacqua1996.exceptions.LocadoraException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    @Parameterized.Parameter
    public List<Filme> filmes;
    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;
    @Parameterized.Parameter(value = 2)
    public String identificacao;

    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private LocacaoDAO locacaoDAO;
    @Mock
    private SPCService spcService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private static Filme filme1 = FilmeBuilder.umFilme().comValor(10.0).agora();
    private static Filme filme2 =  FilmeBuilder.umFilme().comValor(10.0).agora();
    private static Filme filme3 =  FilmeBuilder.umFilme().comValor(10.0).agora();
    private static Filme filme4 = FilmeBuilder.umFilme().comValor(10.0).agora();
    private static Filme filme5 = FilmeBuilder.umFilme().comValor(10.0).agora();
    private static Filme filme6 = FilmeBuilder.umFilme().comValor(10.0).agora();
    private static Filme filme7 = FilmeBuilder.umFilme().comValor(10.0).agora();

    @Parameterized.Parameters(name = "Teste {2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][] {
                {Arrays.asList(filme1, filme2, filme3), 27.5, "3 filmes - 25%"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 32.5, "4 filmes - 50%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 35.0, "5 filmes - 75%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 35.0, "6 filmes - 100%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 45.0, "7 filmes - Sem desconto"}
        });
    }

    @Test
    public void alugaFilmeComDescontosCrescentes() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        Assert.assertEquals(locacao.getValor(), valorLocacao, 0.1);

    }
}
