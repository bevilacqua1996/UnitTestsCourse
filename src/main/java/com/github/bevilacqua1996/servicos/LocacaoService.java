package com.github.bevilacqua1996.servicos;

import com.github.bevilacqua1996.daos.LocacaoDAO;
import com.github.bevilacqua1996.entidades.Filme;
import com.github.bevilacqua1996.entidades.Locacao;
import com.github.bevilacqua1996.entidades.Usuario;
import com.github.bevilacqua1996.exceptions.FilmeSemEstoqueException;
import com.github.bevilacqua1996.exceptions.LocadoraException;
import com.github.bevilacqua1996.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocacaoService {

	public LocacaoDAO locacaoDAO;
	public SPCService spcService;

	public EmailService emailService;

	public Locacao alugarFilme(Usuario usuario, final List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {

		if (usuario == null) {
			throw new LocadoraException("Usuario Vazio");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme Vazio");
		}

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme sem estoque");
			}
		}

		boolean negativado;
		try {
			negativado = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("Problemas com SPC, tente novamente");
		}

		if (negativado) {
			throw new LocadoraException("Usuário Negativado!");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		locacao.setValor(calculaValor(filmes));

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);

		//Salvando a locacao...	
		this.locacaoDAO.salvar(locacao);

		return locacao;
	}

	private Double calculaValor(List<Filme> filmes) {
		Double valorTotal = 0.0;
		for(int i = 0; i < filmes.size(); i ++) {
			switch (i) {
				case 2:
					valorTotal += filmes.get(i).getPrecoLocacao() * 0.75;
					break;
				case 3:
					valorTotal += filmes.get(i).getPrecoLocacao() * 0.5;
					break;
				case 4:
					valorTotal += filmes.get(i).getPrecoLocacao() * 0.25;
					break;
				case 5:
					break;
				default:
					valorTotal += filmes.get(i).getPrecoLocacao();
			}
		}

		return valorTotal;
	}

	public void notificarAtrasos() {
		List<Locacao> locacoes = locacaoDAO.obterLocacoesPendentes();
		for(Locacao locacao : locacoes) {
			if(locacao.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(locacao.getUsuario());
			}
		}
	}

	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor()*dias);
		locacaoDAO.salvar(novaLocacao);
	}

	public static void main(String[] args) {

	}
}