package uece.game;

import java.util.concurrent.Semaphore;

public class Jogadores implements Runnable {
	private String nome;
	private Cartas jogo[];		//Mao do jogador
	private Filas filaDireita; 	//De onde ele retira cartas
	private Filas filaEsquerda;	//Onde ele descarta as cartas
	private int tamanho; 		//Jogo de 4 cartas ou de 5
	private int tentativas;		//Numero de vezes que vai tentar com um par
	//Semaforos
    private Semaphore semEsquerda;
    private Semaphore semDireita;
    private Semaphore semBaralho;
    private Semaphore semGanhei;
    
    private Baralho baralho;	//Baralho para popular os jogadores
    private boolean ganhei;		//Variavel para terminar o jogo
    private int livre;			//Guarda posicao na mao que esta livre
    private int[] cards; 		//Vetor auxiliar para verificar o jogo
    private int maxCartas;		//Guarda a maior combinacao que o jogador possui
    private int minCartas;		//Guarda a menor combinacao que o jogador possui
    private Cartas descarte;	//Carta que sera descartada
    private int numeroDescarte;	//Auxiliar para descarte
	
	public Jogadores(String name, int tam, Filas left, Filas right, Semaphore semLeft, 
			Semaphore semRight, Baralho bar, Semaphore semBar, boolean win, Semaphore semW){
		this.nome = name;
		this.tamanho = tam;
		this.jogo = new Cartas[tam];
		this.filaDireita = right;
		this.filaEsquerda = left;
		this.tentativas = 0;
		this.semEsquerda = semLeft;
		this.semDireita = semRight;
		this.baralho = bar;
		this.semBaralho = semBar;
		this.ganhei = win;
		this.semGanhei = semW;
		this.cards = new int[7];
		this.cards[0] = 10;
		this.numeroDescarte = 6;
		this.livre = 4;
		this.popular();
	}

	private void popular() {
		try{
			this.semBaralho.acquire();
			for(int i=0; i<4; i++){
				this.jogo[i] = this.baralho.distribui();
			}
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		finally{
			this.semBaralho.release();
		}
	}

	@Override
	public void run() {
		//while(this.baralho.distribuicao < 24){};
		this.contaCartas();
		this.ganhei();
		if(this.tamanho == 4){
			while(!ganhei){
				this.descarta();
				System.out.println(this.nome + " descartou");
				this.pegaCarta();
				System.out.println(this.nome + " pegou carta");
				this.contaCartas();
				System.out.println(this.nome + " contou cartas");
				this.ganhei();
			}
		}
	}

	private void pegaCarta() {
		Cartas aux = null;
		while(aux == null){
			try {
				this.semDireita.acquire();
		        aux = this.filaDireita.retiraCarta();
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    } finally {
		    	this.semEsquerda.release();
		    }
			/*for(int i=0; i<this.tamanho; i++){
				if(this.jogo[i] == null){
					this.jogo[i] = aux;
					break; //Sai do laco for
				}
			}*/
			this.jogo[this.livre] = aux;
		}
	}

	private void contaCartas() {
		this.zeraVetor();
		for(int i=0; i<this.tamanho; i++){
			switch (this.jogo[i].getNumero()){
			case 1:
				cards[1]++; break;
			case 2:
				cards[2]++; break;
			case 3:
				cards[3]++; break;
			case 4:
				cards[4]++; break;
			case 5:
				cards[5]++; break;
			case 6:
				cards[6]++; break;
			}
		}
		for(int i=1; i<this.tamanho; i++){
			if(cards[i] > 0 && cards[i] > this.maxCartas){
				this.maxCartas = cards[i];
			}
			if(cards[i] > 0 && cards[i] < this.minCartas){
				this.minCartas = cards[i];
			}
		}
	}
	
	private void ganhei() {
		if(this.maxCartas == 4){
			try {
		        this.semGanhei.acquire();
		        if(!this.ganhei) {
		        	this.ganhei = true;
		        	System.out.println(this.nome + "ganhou!!!");
		        }
		    } catch (InterruptedException e) {
		        e.printStackTrace();
			} finally {
		        this.semGanhei.release();
		    }
		}
	}
	
	private void descarta(){
		this.escolheCarta();
		try {
			this.semEsquerda.acquire();
	        this.filaEsquerda.incluiCarta(this.descarte);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    } finally {
	    	this.semEsquerda.release();
	    }
	}
	
	private void escolheCarta() {
		//Na mao de 4, pode ser trinca, um par, ou todas as cartas diferentes
		//Na mao de 5, inclui dois pares
		if(this.minCartas == 1) {
			if(this.maxCartas == 2) {
				if(this.tentativas < 10){
					this.tentativas++;
					this.escolheSimples();
				} else {
					this.tentativas = 0;
					this.escolhePar();
				}
			} else {
				this.escolheSimples();
			}
		} 
		//Na mao de 4, no caso de ter dois pares
		//Na mao de 5, no caso de ter um par e uma trinca
		else {
			this.escolhePar();
		}
		//Depois de escolher, tira da mao para descartar
		for(int x=0; x<this.tamanho; x++){
			if(this.jogo[x].getNumero() == this.numeroDescarte){
				this.livre = x;
				break; //Sair do laco assim que escolher a carta
			}
		}
		this.descarte = this.jogo[this.livre];
		this.jogo[livre] = null;
	}

	private void escolhePar() {
		//Pega o numero da carta que tem par e sera descartada
		for(int i=1; i<=6; i++){
			if(this.cards[i] == 2){ //Escolhe uma carta que tenha par
				this.numeroDescarte = i;
				break;
			}
		}
	}

	private void escolheSimples() {
		this.numeroDescarte = 0;
		//Escolhe o numero da carta que sera descartada
		for(int i=1; i<=6; i++){
			if(this.cards[i] == this.minCartas){
				this.numeroDescarte = i;
				break;
			}
		}
	}

	private void zeraVetor() {
		for(int i=1; i<=6; i++) cards[i] = 0; 	//Inicializar com 0
		this.maxCartas = 0;						//Guarda maior jogo par ou trinca
		this.minCartas = 10;					//Guarda menor jogo carta simples ou par
	}
	
	public void imprimeMao(){
		System.out.println("Mão do " + this.nome);
		for(int i=0; i<this.tamanho; i++){
			if(this.jogo[i] != null) System.out.println(" " + this.jogo[i]);
		}
		System.out.println("");
	}
}
