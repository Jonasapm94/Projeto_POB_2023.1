/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/
package modelo;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class IngressoIndividual extends Ingresso {

	public IngressoIndividual(int codigo) {
		super(codigo);
	}
	
	public IngressoIndividual(){}
	
	public double calcularValor() {
		return 1.2 * this.jogos.get(0).getPreco();
	}

	public Jogo getJogo() {
		return this.jogos.get(0);
	}

	public void setJogo(Jogo jogo) {
		this.jogos.add(0,jogo);
		jogo.setEstoque(jogo.getEstoque() - 1);
	}

	@Override
	public String toString() {
		return "codigo=" + codigo + ", jogo=" + jogos.get(0).getId();
	}
	
	
	
}
