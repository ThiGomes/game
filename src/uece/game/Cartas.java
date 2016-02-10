package uece.game;

public class Cartas {
	private String naipe;
	private int numero;
	
	public Cartas (int num, String N){
		this.numero = num;
		this.naipe = N;
	}
	
	public int getNumero(){
		return this.numero;
	}
	
	@Override
	public String toString() {
		return numero + naipe;
	}

}
