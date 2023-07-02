/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/
package modelo;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
@Entity
@DiscriminatorValue("Grupo")
public class IngressoGrupo extends Ingresso {

	public IngressoGrupo(int codigo) {
		super(codigo);
	}

	public double calcularValor() {
		double soma=0;
		for(Jogo j : this.jogos)
			soma = soma + j.getPreco();
		
		return 0.9 * soma;
	}
	
	public void adicionar(Jogo j){
		this.jogos.add(j);
	}
	public void remover(Jogo j){
		this.jogos.remove(j);
	}
	public Jogo localizar(int id){
		for(Jogo j : this.jogos){
			if(j.getId() == id)
				return j;
		}
		return null;
	}

	public List<Jogo> getJogos() {
		return this.jogos;
	}

	@Override
	public String toString() {
		String texto = "codigo=" + codigo + ", jogos:";
		
		for(Jogo j : this.jogos)
			texto += j.getId() + ",";
		
		return texto;
	}
	
	
}
