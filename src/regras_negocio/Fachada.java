/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package regras_negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import daodb4o.DAO;
import daodb4o.DAOIngresso;
import daodb4o.DAOIngressoGrupo;
import daodb4o.DAOIngressoIndividual;
import daodb4o.DAOJogo;
import daodb4o.DAOTime;
import daodb4o.DAOUsuario;
import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;
import modelo.Jogo;
import modelo.Time;
import modelo.Usuario;

public class Fachada {
	private Fachada() {}	

	private static DAOUsuario daousuario = new DAOUsuario(); 
	private static DAOTime daotime = new DAOTime(); 
	private static DAOJogo daojogo = new DAOJogo(); 
	private static DAOIngresso daoingresso = new DAOIngresso(); 
	private static DAOIngressoIndividual daoingressoindividual = new DAOIngressoIndividual(); 
	private static DAOIngressoGrupo daoingressogrupo = new DAOIngressoGrupo(); 


	public static Usuario logado;
	public static void inicializar(){

	}
	public static void finalizar(){

	}


	public static List<Time> listarTimes() {
		//retorna todos os times
		DAO.begin();
		List<Time> times = daotime.listarTimes();
		DAO.commit();
		return times;
	}
	public static List<Jogo> listarJogos() {
		//retorna todos os jogos
		DAO.begin();
		List<Jogo> jogos = daojogo.listarJogos();
		DAO.commit();
		return jogos;
	}
	public static List<Usuario> listarUsuarios() {
		//retorna todos os jogos
		DAO.begin();
		List<Usuario> usuarios = daousuario.listarUsuarios();
		DAO.commit();
		return usuarios;
	}
	public static List<Ingresso> listarIngressos() {
		//retorna todos os ingressos 
		DAO.begin();
		List<Ingresso> ingressos = daoingresso.listarIngressos();
		DAO.commit();
		return ingressos;
	}
	public static List<Jogo> listarJogos(String data) {
		//retorna os jogos na data fornecida (query)
		DAO.begin();
		List<Jogo> jogos = daojogo.listarJogos(data);
		DAO.commit();

		return jogos;
	}
	public static Ingresso localizarIngresso(int codigo) {
		//retorna o ingresso com o c�digo fornecido
		DAO.begin();
		Ingresso ingresso = daoingresso.read(codigo);
		DAO.commit();
		return ingresso;
	}

	public static Jogo	localizarJogo(int id) {
		//retorna o jogo com o id fornecido
		DAO.begin();
		Jogo jogo = daojogo.read(id);
		DAO.commit();
		return jogo;
	}

	public static Usuario criarUsuario(String email, String senha) throws Exception{
		DAO.begin(); 
		Usuario usu = daousuario.read(email);
		if (usu!=null) {
			DAO.rollback();
			throw new Exception("Usuario ja cadastrado: " + email);
		}
		usu = new Usuario(email, senha);

		daousuario.create(usu);
		DAO.commit();
		return usu;
	}
	public static Usuario validarUsuario(String email, String senha) {
		DAO.begin();
		Usuario usu = daousuario.read(email);
		if (usu==null)
			return null;

		if (! usu.getSenha().equals(senha))
			return null;

		DAO.commit();
		return usu;
	}

	public static Time 	criarTime(String nome, String origem) throws Exception {
		DAO.begin();
		//verificar regras de negocio
		Time timeTemp = daotime.read(nome);
		if (timeTemp.getNome() == nome){
			DAO.rollback();
			throw new Error("Nome de time já existe.");
		}
		//criar o time
		Time time = new Time(nome,origem);
		
		//gravar time no banco
		daotime.create(time);
		DAO.commit();
		return null;
	}

	public static Jogo 	criarJogo(String data, String local, int estoque, double preco, String nometime1, String nometime2)  throws Exception {
		DAO.begin();
		//verificar regras de negocio

		//RN4
		List<Jogo> jogos = daojogo.readAll();
		int maxId = 0;
		for(Jogo jogo : jogos){
			if(jogo.getId() > maxId){
				maxId = jogo.getId() + 1;
			}
		}

		//RN5


	
		//localizar time1 e time2
		//criar  jogo 
		Jogo jogo = new Jogo(data, local, estoque, preco);

		//relacionar o jogo com os times e vice-versa 
		jogo.setTime1(time1);
		jogo.setTime2(time2);
		time1.adicionar(jogo);
		time2.adicionar(jogo);
		
		//gravar jogo no banco
		DAO.commit();
		return null;
	}

	public static IngressoIndividual criarIngressoIndividual(int id) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		//gerar codigo aleat�rio 
		int codigo = new Random().nextInt(1000000);

		//verificar unicididade do codigo no sistema 
		
		
		//criar o ingresso individual 
		IngressoIndividual ingresso = new IngressoIndividual(codigo);

		//relacionar este ingresso com o jogo e vice-versa
		ingresso.setJogo(jogo);
		jogo.adicionar(ingresso);
		jogo.setEstoque(jogo.getEstoque()-1);

		//gravar ingresso no banco
		DAO.commit();
		return null;
	}

	public static IngressoGrupo	criarIngressoGrupo(int[] ids) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		//gerar codigo aleat�rio 
		int codigo = new Random().nextInt(1000000);
		
		//verificar unicididade no sistema 
		
		//criar o ingresso grupo 
		IngressoGrupo ingresso = new IngressoGrupo(codigo);
		
		//relacionar este ingresso com os jogos indicados e vice-versa
		for (Jogo j: ids) {
			j.adicionar(ingresso);
			j.setEstoque(j.getEstoque()-1);
			ingresso.adicionar(j);
		}
		
		//gravar ingresso no banco
		
		
		DAO.commit();
		return null;
	}

	public static void	apagarIngresso(int codigo) throws Exception {
		DAO.begin();
		//o codigo refere-se a ingresso individual ou grupo
		//verificar regras de negocio
		//remover o relacionamento entre o ingresso e o(s) jogo(s) ligados a ele

		Ingresso ingresso = daoingresso.read(codigo);
		if (ingresso instanceof IngressoGrupo grupo) {
			ArrayList<Jogo> jogos = grupo.getJogos();
			for (Jogo j : jogos) {
				j.remover(grupo);
				j.setEstoque(j.getEstoque()+1);
			}
		}
		else 
			if (ingresso instanceof IngressoIndividual individuo) {
				Jogo jogo = individuo.getJogo();
				jogo.remover(individuo);
				jogo.setEstoque(jogo.getEstoque()+1);
			}

		//apagar ingresso no banco

		DAO.commit();
	}

	public static void	apagarTime(int id) throws Exception {
		DAO.begin();
		//verificar regras de negocio
		//apagar time no banco
		DAO.commit();
	}

	public static void 	apagarJogo(int id) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		//apagar jogo no banco
		DAO.commit();
	}

	/**********************************
	 * 5 Consultas
	 **********************************/
}
