package uece.game;

import java.util.concurrent.Semaphore;

public class Game {

	public static void main(String[] args) {
		Baralho baralho = new Baralho();
		boolean ganhei = false;
		
		//Semaforos para exclusao mutua
		Semaphore semBaralho = new Semaphore(1);
		Semaphore semGanhou = new Semaphore(1);
		Semaphore sem1 = new Semaphore(1);
		Semaphore sem2 = new Semaphore(1);
		Semaphore sem3 = new Semaphore(1);
		Semaphore sem4 = new Semaphore(1);
		
		//Filas compartilhadas
		Filas fila1 = new Filas(3, baralho);
		Filas fila2 = new Filas(3, baralho);
		Filas fila3 = new Filas(3, baralho);
		Filas fila4 = new Filas(3, baralho);
		
		//Cria as threads
		Jogadores pl1 = new Jogadores("Pedro", 4, fila1, fila2, sem1, sem2, baralho, semBaralho, ganhei, semGanhou);
		Jogadores pl2 = new Jogadores("Tiago", 4, fila2, fila3, sem2, sem3, baralho, semBaralho, ganhei, semGanhou);
		Jogadores pl3 = new Jogadores("João", 4, fila3, fila4, sem3, sem4, baralho, semBaralho, ganhei, semGanhou);
		Jogadores pl4 = new Jogadores("Nobarquinho", 4, fila4, fila1, sem4, sem1, baralho, semBaralho, ganhei, semGanhou);
		
		//Executa as threads
		new Thread(pl1).start();		
		new Thread(pl2).start();
		new Thread(pl3).start();
		new Thread(pl4).start();
		
		//Quando alguem ganhar imprime o resultado
		pl1.imprimeMao();
		pl2.imprimeMao();
		pl3.imprimeMao();
		pl4.imprimeMao();
		
	}

}
