package uece.game;

import java.util.Random;

public class Baralho {
	private int tamanho = 24;
	public Cartas[] baralho;
	public int distribuicao;
	public boolean disponivel;
	
	public Baralho(){
		this.baralho = new Cartas[this.tamanho];
		this.popular();
		this.embaralhar();
		this.distribuicao = 0;
		this.disponivel = true;
	}

	private void popular() {
		int posicao;
		for(int i=1; i<=6; i++){
			posicao = i-1;
			this.baralho[posicao] = new Cartas(i, "-Ouro");
			posicao+=6;
			this.baralho[posicao] = new Cartas(i, "-Copas");
			posicao+=6;
			this.baralho[posicao] = new Cartas(i, "-Espada");
			posicao+=6;
			this.baralho[posicao] = new Cartas(i, "-Paus");
		}
	}

	private void embaralhar() {
		Random r = new Random();
		Cartas carta;
		for (int i=0; i<this.baralho.length; i++) {
			int j = r.nextInt(this.baralho.length);
			carta = this.baralho[i];
			this.baralho[i] = this.baralho[j];
			this.baralho[j] = carta;
			//this.exibir();
		}
	}
	
	private void exibir() {
		for(int i=0; i<this.tamanho; i++){
			System.out.print(this.baralho[i] + " ");
		}
	}

	public Cartas distribui(){
		Cartas carta = this.baralho[this.distribuicao];
		this.baralho[this.distribuicao] = null;
		this.distribuicao++;
		return carta;
	}

}
