package com.github.bevilacqua1996.servicos;

import com.github.bevilacqua1996.builders.FilmeBuilder;
import com.github.bevilacqua1996.builders.LocacaoBuilder;
import com.github.bevilacqua1996.builders.UsuarioBuilder;
import com.github.bevilacqua1996.daos.LocacaoDAO;
import com.github.bevilacqua1996.entidades.Filme;
import com.github.bevilacqua1996.entidades.Locacao;
import com.github.bevilacqua1996.entidades.Usuario;
import com.github.bevilacqua1996.exceptions.FilmeSemEstoqueException;
import com.github.bevilacqua1996.exceptions.LocadoraException;
import com.github.bevilacqua1996.matchers.DiaAtualSemanaMatcher;
import com.github.bevilacqua1996.matchers.MatchersProprios;
import com.github.bevilacqua1996.utils.DataUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class, DataUtils.class, DiaAtualSemanaMatcher.class})
public class LocacaoServiceTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private LocacaoService locacaoService;
    private List<Filme> filmes;

    @Mock
    private SPCService spcService;
    @Mock
    private LocacaoDAO locacaoDAO;

    @Mock
    private EmailService emailService;

    @Before
    public void setupTestVariables(){
        filmes = new ArrayList<>();
        MockitoAnnotations.initMocks(this);
        locacaoService = PowerMockito.spy(locacaoService);
    }

    @Test
    public void deveAlugarFilmeSemCalcularValor() throws Exception {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        PowerMockito.doReturn(1.0).when(locacaoService, "calculaValor", filmes);

        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        Assert.assertEquals(locacao.getValor(), 1.0, 0.01);

        PowerMockito.verifyPrivate(locacaoService).invoke("calculaValor", filmes);
    }

    @Test
    public void deveCalcularValorLocacao() throws Exception {
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        Double valor = (Double) Whitebox.invokeMethod(locacaoService, "calculaValor", filmes);

        Assert.assertEquals(valor, 5.0, 0.01);
    }

    @Test
    public void alugaFilmes() throws Exception {

        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme1 = FilmeBuilder.umFilme().agora();
        Filme filme2 = FilmeBuilder.umFilme().agora();

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(14, 10, 2022));

        filmes.add(filme1);
        filmes.add(filme2);

        Locacao locacao;

        locacao = this.locacaoService.alugarFilme(usuario, filmes);

        errorCollector.checkThat(locacao.getValor(), is(equalTo(filme1.getPrecoLocacao() + filme2.getPrecoLocacao())));
        errorCollector.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
        errorCollector.checkThat(locacao.getDataRetorno(), MatchersProprios.ehDiaSeguinte());

    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        locacaoService = new LocacaoService();
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme = FilmeBuilder.umFilmeSemEstoque().agora();

        filmes.add(filme);

        locacaoService.alugarFilme(usuario, filmes);

    }

    @Test
    public void naoDeveAulgarFilmeSemEstoque2() {

        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme = FilmeBuilder.umFilmeSemEstoque().agora();

        filmes.add(filme);

        try {
            locacaoService.alugarFilme(usuario, filmes);
            Assert.fail("Deveria lançar exception");
        } catch (Exception e) {
            Assert.assertEquals("Filme sem estoque", e.getMessage());
        }

    }

    @Test
    public void naoDeveAulgarFilmeSemEstoque3() throws Exception {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme = FilmeBuilder.umFilmeSemEstoque().agora();

        filmes.add(filme);

        expectedException.expect(FilmeSemEstoqueException.class);
        expectedException.expectMessage("Filme sem estoque");

        locacaoService.alugarFilme(usuario, filmes);

    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        Filme filme = FilmeBuilder.umFilme().agora();

        filmes.add(filme);

        try {
            locacaoService.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException ex) {
            Assert.assertEquals("Usuario Vazio", ex.getMessage());
        }
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme Vazio");

        locacaoService.alugarFilme(usuario, null);

    }

    @Test
    public void naoDeveDevolverFilmeNoDomingo() throws Exception {

        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(15, 10, 2022));

        Locacao retorno = locacaoService.alugarFilme(usuario, filmes);

        Assert.assertThat(retorno.getDataRetorno(), MatchersProprios.caiNaSegunda());

        PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();
    }

    @Test
    public void naoDeveAlugarFilmeNegativadoSPC() throws Exception {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        Mockito.when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

        try {
            locacaoService.alugarFilme(usuario, filmes);
            Assert.fail();
        } catch (LocadoraException ex) {
            Assert.assertEquals(ex.getMessage(), "Usuário Negativado!");
        }

        Mockito.verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaNotificacoesAtrasadas() {

        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuário em dia").agora();
        Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();

        List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.
                umLocacao().
                    atrasado().
                    comUsuario(usuario).
                    agora(), LocacaoBuilder.
                umLocacao().comUsuario(usuario2).agora(), LocacaoBuilder.
                umLocacao().atrasado().comUsuario(usuario3).agora(), LocacaoBuilder.
                umLocacao().atrasado().comUsuario(usuario3).agora());

        Mockito.when(locacaoDAO.obterLocacoesPendentes()).thenReturn(locacoes);

        locacaoService.notificarAtrasos();

        Mockito.verify(emailService, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));

        Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
        Mockito.verify(emailService).notificarAtraso(usuario);
        Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
        Mockito.verifyNoMoreInteractions(emailService);
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        Mockito.when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha!"));

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Problemas com SPC, tente novamente");

        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarLocacao() {
        Locacao locacao = LocacaoBuilder.umLocacao().agora();

        locacaoService.prorrogarLocacao(locacao, 3);

        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(locacaoDAO).salvar(argumentCaptor.capture());

        Locacao locacaoRetornada = argumentCaptor.getValue();

        errorCollector.checkThat(locacaoRetornada.getValor(), is(12.0));
        errorCollector.checkThat(locacaoRetornada.getDataLocacao(), MatchersProprios.ehHoje());
        errorCollector.checkThat(locacaoRetornada.getDataRetorno(), MatchersProprios.ehDiaPosterior(3));
    }

}
