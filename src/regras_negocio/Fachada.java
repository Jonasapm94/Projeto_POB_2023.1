/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package regras_negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import daodb4o.*;
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
		DAO.open();
	}
	public static void finalizar(){
		DAO.close();
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

		if (timeTemp != null){
			DAO.rollback();
			throw new Exception("Nome de time já existe.");
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
		int newId = daojogo.gerarId();

		//RN5
		if(nometime1.equals(nometime2)){
			DAO.rollback();
			throw new Exception("Nomes de times iguais");
		}

		//localizar time1 e time2
		Time time1 = daotime.read(nometime1);
		if(time1 == null){
			DAO.rollback();
			throw new Exception("Não existe um time com este nome: " + nometime1);
		}

		Time time2 = daotime.read(nometime2);
		if(time2 == null){
			DAO.rollback();
			throw new Exception("Não existe um time com este nome: " + nometime2);
		}


		//criar  jogo 
		Jogo jogo = new Jogo(data, local, estoque, preco);
		jogo.setId(newId);

		//relacionar o jogo com os times e vice-versa 
		jogo.setTime1(time1);
		jogo.setTime2(time2);
		time1.adicionar(jogo);
		time2.adicionar(jogo);
		
		//gravar jogo no banco
		daojogo.create(jogo);
		DAO.commit();
		return null;
	}

	public static IngressoIndividual criarIngressoIndividual(int idJogo) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		//gerar codigo aleat�rio
		//verificar unicididade do codigo no sistema
		List<Ingresso> ingressos = daoingresso.listarIngressos();

		int codigo;
		boolean flag;

		do {
			codigo = new Random().nextInt(1000000);
			flag = false;
			for( Ingresso ingresso : ingressos){
				if(ingresso.getCodigo() == codigo){
					flag = true;
				}
			}
		} while (flag);


		//criar o ingresso individual 
		IngressoIndividual ingresso = new IngressoIndividual(codigo);

		//relacionar este ingresso com o jogo e vice-versa
		Jogo jogo = daojogo.read(idJogo);

		if(jogo == null){
			DAO.rollback();
			throw new Exception("Nao existe um jogo com este id");
		}

		ingresso.setJogo(jogo);
		jogo.adicionar(ingresso);
		jogo.setEstoque(jogo.getEstoque()-1);

		//gravar ingresso no banco
		daoingresso.create(ingresso);
		DAO.commit();
		return null;
	}

	public static IngressoGrupo	criarIngressoGrupo(int[] ids) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		//gerar codigo aleat�rio
		//verificar unicididade no sistema
		List<Ingresso> ingressos = daoingresso.listarIngressos();

		int codigo;
		boolean flag;

		do {
			codigo = new Random().nextInt(1000000);
			flag = false;
			for( Ingresso ingresso : ingressos){
				if(ingresso.getCodigo() == codigo){
					flag = true;
				}
			}
		} while (flag);

		//criar o ingresso grupo 
		IngressoGrupo ingresso = new IngressoGrupo(codigo);
		
		//relacionar este ingresso com os jogos indicados e vice-versa
		for (int id : ids){
			Jogo jogo = daojogo.read(id);
			if (jogo == null){
				DAO.rollback();
				throw new Exception("Não existe um jogo com este id informado: " + id);
			}
			jogo.adicionar(ingresso);
			jogo.setEstoque(jogo.getEstoque()-1);
			ingresso.adicionar(jogo);
		}
		//gravar ingresso no banco
		daoingresso.create(ingresso);
		DAO.commit();
		return null;
	}

	public static void	apagarIngresso(int codigo) throws Exception {
		DAO.begin();
		//o codigo refere-se a ingresso individual ou grupo
		//verificar regras de negocio

		//remover o relacionamento entre o ingresso e o(s) jogo(s) ligados a ele

		Ingresso ingresso = daoingresso.read(codigo);
		if (ingresso == null){
			DAO.rollback();
			throw new Exception("Nao existe ingresso com este código");
		}
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
		daoingresso.delete(ingresso);
		DAO.commit();
	}

	public static void	apagarTime(String nome) throws Exception {
		DAO.begin();
		//verificar regras de negocio
		//apagar time no banco
		Time time = daotime.read(nome);
		if(time == null){
			DAO.rollback();
			throw new Exception("Nao existe time com este nome.");
		}
		if(time.getJogos().size() > 0){
			DAO.rollback();
			throw new Exception ("Time possui jogos. Não podera ser excluido");
		}
		daotime.delete(time);
		DAO.commit();
	}

	public static void 	apagarJogo(int id) throws Exception{
		DAO.begin();
		//verificar regras de negocio
		//apagar jogo no banco
		Jogo jogo = daojogo.read(id);
		if (jogo == null){
			DAO.rollback();
			throw new Exception("Nao existe jogo com o id informado.");
		}
		if (jogo.getIngressos().size() > 0){
			DAO.rollback();
			throw new Exception("Jogo possui ingressos vendidos, não pode ser excluido.");
		}
		daojogo.delete(jogo);
		DAO.commit();
	}

	/**********************************
	 * 5 Consultas
	 **********************************/

//	Consulta 1: Quais as datas que time x joga
	public static List<Jogo> listarJogosDoTime(String nomeTime) throws Exception{
		DAO.begin();
		Time time = daotime.read(nomeTime);
		if (time == null){
			DAO.rollback();
			throw new Exception("Nao existe um time com este nome");
		}
		List<Jogo> jogos = time.getJogos();


		DAO.commit();
		return jogos;
	}


	// Consulta 2: Quantos ingressos individuais um determinado time vendeu
	public static int numIngressosIndividuaisVendidosPorTime(String nomeTime) throws Exception {
		List<Jogo> jogosDoTime = listarJogosDoTime(nomeTime);
		int somaIng = 0;
		for(Jogo jogo : jogosDoTime){
			List<Ingresso> ingressosDoJogo = jogo.getIngressos();
			for(Ingresso ingresso : ingressosDoJogo){
				if (ingresso instanceof IngressoIndividual){
					somaIng++;
				}
			}
		}
		return somaIng;
	}

	//Consulta 3: Quantos ingressos-grupo foram vendidos por time
	public static int numIngressosGrupoVendidosPorTime(String nomeTime) throws Exception {
		List<Jogo> jogosDoTime = listarJogosDoTime(nomeTime);
		int somaIng = 0;
		for(Jogo jogo : jogosDoTime){
			List<Ingresso> ingressosDoJogo = jogo.getIngressos();
			for(Ingresso ingresso : ingressosDoJogo){
				if (ingresso instanceof IngressoGrupo){
					somaIng++;
				}
			}
		}
		return somaIng;
	}

	//Consulta 4: Qual o estoque de ingressos disponíveis para os jogos de um determinado time?
	public static int estoqueTotalDoTime(String nomeTime) throws Exception {
		List<Jogo> jogos = listarJogosDoTime(nomeTime);
		int estoqueTotal = 0;
		for(Jogo jogo: jogos){
			estoqueTotal += jogo.getEstoque();
		}
		return estoqueTotal;
	}

	//Consulta 5: Quais os códigos dos ingressos de todos os jogos de um time?
//	public static List<int> co
}
