/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package modelo;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
//@DiscriminatorColumn(name="tipo", discriminatorType = DiscriminatorType.INTEGER)
//@DiscriminatorValue("1")
public abstract class Ingresso  {
	
	@Id
    protected int codigo;

	@ManyToMany(mappedBy = "ingressos", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	protected List<Jogo> jogos = new ArrayList<Jogo>();
	
	public Ingresso(){}
	
	public Ingresso(int codigo) {
		this.codigo = codigo;
	}


	public abstract double calcularValor();

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	

}
