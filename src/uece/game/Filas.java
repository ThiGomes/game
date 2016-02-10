package uece.game;

public class Filas {
	private int tamanho;
	private Cartas[] fila;
	private int numeroDeCartas;
	private int head;
	private int tail;
	
	public Filas(int tam, Baralho bar){
		this.tamanho = tam;
		this.fila = new Cartas[tam];
		this.numeroDeCartas = 0;
		this.head = 0;
		this.tail = 0;
		for(int i=0; i<2; i++){
			fila[i] = bar.distribui(); 
		}
	}
	
	public void incluiCarta(Cartas carta){
		if(this.numeroDeCartas < this.tamanho){		//Nao insere se tiver cheia
			if(this.fila[this.head] == null){ 		
				this.fila[this.head] = carta; 		//Insere na head
				this.numeroDeCartas++;				//Incrementa o numero de cartas
				this.head++;						//Incrementa a posicao de insercao
				this.head %= this.tamanho;			//Se chegar no limite, volta ao comeco
			}
		} else {
			System.out.println("Fila cheia!");
		}
	}
	
	public Cartas retiraCarta(){
		Cartas carta;
		if(this.fila[this.tail] != null){	//Checa se a posicao esta vazia
			carta = this.fila[this.tail];	//Recebe a carta inserida a mais tempo
			this.fila[this.tail] = null;	//Retira a carta da fila
			this.numeroDeCartas--;			//Diminui quantidade de cartas na fila
			this.tail++;					//Muda para a posicao da proxima carta que ira sair
			this.tail %= this.tamanho;		//Se for o final da fila, volta ao comeco
			return carta;					//Retorna a carta
		} else {
			System.out.println("Fila vazia!");
			return null;					//Se nao tiver carta nao retorna nada
		}
	}
	
	public boolean isFull(){ //Checar antes de bloquear a insercao
		if(this.numeroDeCartas == this.tamanho) return true;
		else return false;
	}
	
	public boolean isEmpty(){ //Checar antes de bloquear a insercao
		if(this.numeroDeCartas == 0) return true;
		else return false;
	}
	
}
