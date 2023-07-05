/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package regras_negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import daojpa.DAOIngressoGrupoJPA;
import daojpa.DAOIngressoIndividualJPA;
import daojpa.DAOIngressoJPA;
import daojpa.DAOJPA;
import daojpa.DAOJogoJPA;
import daojpa.DAOTimeJPA;
import daojpa.DAOUsuarioJPA;
import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;
import modelo.Jogo;
import modelo.Time;
import modelo.Usuario;

public class Fachada {
	private Fachada() {}	

	private static DAOUsuarioJPA daousuario = new DAOUsuarioJPA(); 
	private static DAOTimeJPA daotime = new DAOTimeJPA(); 
	private static DAOJogoJPA daojogo = new DAOJogoJPA(); 
	private static DAOIngressoJPA daoingresso = new DAOIngressoJPA(); 
	private static DAOIngressoIndividualJPA daoingressoindividual = new DAOIngressoIndividualJPA(); 
	private static DAOIngressoGrupoJPA daoingressogrupo = new DAOIngressoGrupoJPA(); 


	public static Usuario logado;
	public static void inicializar(){
		DAOJPA.open();
	}
	public static void finalizar(){
		DAOJPA.close();
	}


	public static List<Time> listarTimes() {
		//retorna todos os times
		DAOJPA.begin();
		List<Time> times = daotime.listarTimes();
		DAOJPA.commit();
		return times;
	}
	
	public static List<Jogo> listarJogos() {
		//retorna todos os jogos
		DAOJPA.begin();
		List<Jogo> jogos = daojogo.listarJogos();
		DAOJPA.commit();
		return jogos;
	}
	
	public static List<Usuario> listarUsuarios() {
		//retorna todos os usuarios
		DAOJPA.begin();
		List<Usuario> usuarios = daousuario.listarUsuarios();
		DAOJPA.commit();
		return usuarios;
	}
	
	public static List<Ingresso> listarIngressos() {
		//retorna todos os ingressos 
		DAOJPA.begin();
		List<Ingresso> ingressos = daoingresso.listarIngressos();
		DAOJPA.commit();
		return ingressos;
	}
	public static List<Jogo> listarJogos(String data) {
		//retorna os jogos na data fornecida (query)
		DAOJPA.begin();
		List<Jogo> jogos = daojogo.listarJogos(data);
		DAOJPA.commit();

		return jogos;
	}
	public static Ingresso localizarIngresso(int codigo) {
		//retorna o ingresso com o c�digo fornecido
		DAOJPA.begin();
		Ingresso ingresso = daoingresso.read(codigo);
		DAOJPA.commit();
		return ingresso;
	}

	public static Jogo	localizarJogo(int id) {
		//retorna o jogo com o id fornecido
		DAOJPA.begin();
		Jogo jogo = daojogo.read(id);
		DAOJPA.commit();
		return jogo;
	}

	public static Usuario criarUsuario(String email, String senha) throws Exception{
		DAOJPA.begin(); 
		Usuario usu = daousuario.read(email);
		if (usu!=null) {
			DAOJPA.rollback();
			throw new Exception("Usuario ja cadastrado: " + email);
		}
		usu = new Usuario(email, senha);

		daousuario.create(usu);
		DAOJPA.commit();
		return usu;
	}
	public static Usuario validarUsuario(String email, String senha) {
		DAOJPA.begin();
		Usuario usu = daousuario.read(email);
		if (usu==null)
			return null;

		if (! usu.getSenha().equals(senha))
			return null;

		DAOJPA.commit();
		return usu;
	}

	public static Time 	criarTime(String nome, String origem) throws Exception {
		DAOJPA.begin();
		if (nome.trim().equals("")) {
			throw new Exception("Nome do time vazio.");
		}
		
		if (origem.trim().equals("")) {
			throw new Exception("Origem do time vazio.");
		}
		
		//verificar regras de negocio
		Time timeTemp = daotime.read(nome);

		if (timeTemp != null){
			DAOJPA.rollback();
			throw new Exception("Nome de time já existe.");
		}
		//criar o time
		Time time = new Time(nome,origem);
		
		//gravar time no banco
		daotime.create(time);
		DAOJPA.commit();
		return time;
	}

	public static Jogo 	criarJogo(String data, String local, int estoque, double preco, String nometime1, String nometime2)  throws Exception {
		DAOJPA.begin();
		//verificar regras de negocio

		if (preco <= 0){
			DAOJPA.rollback();
			throw new Exception("Jogo nao pode ser criado com preco menor ou igual a zero.");
		}

		//RN4
		// int newId = daojogo.gerarId();

		if (estoque <= 0){
			DAOJPA.rollback();
			throw new Exception("Jogo nao pode ser criado com estoque menor ou igual a zero.");

		}

		//RN5
		if(nometime1.equals(nometime2)){
			DAOJPA.rollback();
			throw new Exception("Nomes de times iguais");
		}

		//localizar time1 e time2
		Time time1 = daotime.read(nometime1);
		if(time1 == null){
			DAOJPA.rollback();
			throw new Exception("Não existe um time com este nome: " + nometime1);
		}

		Time time2 = daotime.read(nometime2);
		if(time2 == null){
			DAOJPA.rollback();
			throw new Exception("Não existe um time com este nome: " + nometime2);
		}


		//criar  jogo 
		Jogo jogo = new Jogo(data, local, estoque, preco);
		// jogo.setId(newId);

		//relacionar o jogo com os times e vice-versa 
		jogo.setTime1(time1);
		jogo.setTime2(time2);
		time1.adicionar(jogo);
		time2.adicionar(jogo);
		
		//gravar jogo no banco
		daojogo.create(jogo);
		DAOJPA.commit();
		return jogo;
	}

	public static IngressoIndividual criarIngressoIndividual(int idJogo) throws Exception{
		DAOJPA.begin();
		//verificar regras de negocio
		//gerar codigo aleat�rio
		//verificar unicididade do codigo no sistema
		
		//List<Ingresso> ingressos = daoingresso.listarIngressos();

		int codigo;
		boolean flag;

		do {
			codigo = new Random().nextInt(1000000);
			flag = false;
			if(daoingressoindividual.read(codigo)!=null) {
				flag = true;
			}
		} while (flag);


		//criar o ingresso individual 
		IngressoIndividual ingresso = new IngressoIndividual(codigo);

		//relacionar este ingresso com o jogo e vice-versa
		Jogo jogo = daojogo.read(idJogo);

		if(jogo == null){
			DAOJPA.rollback();
			throw new Exception("Nao existe um jogo com este id informado: " + idJogo);
		}

		ingresso.setJogo(jogo);
		jogo.adicionar(ingresso);
		jogo.setEstoque(jogo.getEstoque()-1);
		daojogo.update(jogo);

		//gravar ingresso no banco
		daoingressoindividual.create(ingresso);
		DAOJPA.commit();
		return ingresso;
	}

	public static IngressoGrupo	criarIngressoGrupo(int[] ids) throws Exception{
		DAOJPA.begin();
		//verificar regras de negocio
		//gerar codigo aleat�rio
		//verificar unicididade no sistema
		
		//List<Ingresso> ingressos = daoingresso.listarIngressos();

		int codigo;
		boolean flag;

		do {
			codigo = new Random().nextInt(1000000);
			flag = false;
			if(daoingressogrupo.read(codigo)!=null) {
				flag = true;
			}
		} while (flag);

		//criar o ingresso grupo
		IngressoGrupo ingresso = new IngressoGrupo(codigo);
		
		
		//relacionar este ingresso com os jogos indicados e vice-versa
		for (int id : ids){
			Jogo jogo = daojogo.read(id);
			if (jogo == null){
				DAOJPA.rollback();
				throw new Exception("Não existe um jogo com este id informado: " + id);
			}
			jogo.adicionar(ingresso);
			jogo.setEstoque(jogo.getEstoque()-1);
			ingresso.adicionar(jogo);
			daojogo.update(jogo);
		}
		//gravar ingresso no banco
		daoingressogrupo.create(ingresso);
		DAOJPA.commit();
		return ingresso;
	}

	public static void	apagarIngresso(int codigo) throws Exception {
		DAOJPA.begin();
		//o codigo refere-se a ingresso individual ou grupo
		//verificar regras de negocio

		//remover o relacionamento entre o ingresso e o(s) jogo(s) ligados a ele

		Ingresso ingresso = daoingresso.read(codigo);
		if (ingresso == null){
			DAOJPA.rollback();
			throw new Exception("Nao existe ingresso com este código");
		}
		if (ingresso instanceof IngressoGrupo grupo) {
			List<Jogo> jogos = grupo.getJogos();
			for (Jogo j : jogos) {
				j.remover(grupo);
				j.setEstoque(j.getEstoque()+1);
				daojogo.update(j);
			}
		}
		else 
			if (ingresso instanceof IngressoIndividual individuo) {
				Jogo jogo = individuo.getJogo();
				jogo.remover(individuo);
				jogo.setEstoque(jogo.getEstoque()+1);
				daojogo.update(jogo);
			}

		//apagar ingresso no banco
		daoingresso.delete(ingresso);
		DAOJPA.commit();
	}

	public static void	apagarTime(String nome) throws Exception {
		DAOJPA.begin();
		//verificar regras de negocio
		//apagar time no banco
		Time time = daotime.read(nome);
		if(time == null){
			DAOJPA.rollback();
			throw new Exception("Nao existe time com este nome.");
		}
		if(time.getJogos().size() > 0){
			DAOJPA.rollback();
			throw new Exception ("Time possui jogos. Não podera ser excluido");
		}
		daotime.delete(time);
		DAOJPA.commit();
	}

	public static void 	apagarJogo(int id) throws Exception{
		DAOJPA.begin();
		//verificar regras de negocio
		//apagar jogo no banco
		Jogo jogo = daojogo.read(id);
		if (jogo == null){
			DAOJPA.rollback();
			throw new Exception("Nao existe jogo com o id informado.");
		}
		if (jogo.getIngressos().size() > 0){
			DAOJPA.rollback();
			throw new Exception("Jogo possui ingressos vendidos, não pode ser excluido.");
		}
		
		Time time1 = jogo.getTime1();
		time1.remover(jogo);
		daotime.update(time1);

		Time time2 = jogo.getTime2();
		time2.remover(jogo);
		daotime.update(time2);

		daojogo.delete(jogo);
		
		DAOJPA.commit();
	}

	/**********************************
	 * 5 Consultas
	 **********************************/

//	Consulta 1: Quais as datas que time x joga
	public static List<Jogo> listarJogosDoTime(String nomeTime) throws Exception{
		DAOJPA.begin();
		Time time = daotime.read(nomeTime);
		if (time == null){
			DAOJPA.rollback();
			throw new Exception("Nao existe um time com este nome");
		}
		List<Jogo> jogos = time.getJogos();


		DAOJPA.commit();
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

	//Consulta 4: Qual o estoque de ingressos disponíveis para cada jogo de um determinado time?
	public static List<Jogo> estoqueTotalDoTime(String nomeTime) throws Exception {
		return listarJogosDoTime(nomeTime);
	}

	//Consulta 5: Quais os códigos dos ingressos de todos os jogos de um time?
	public static List<Ingresso> codigosIngressoPorTime(String nomeTime) throws Exception{
		List<Jogo> jogos = listarJogosDoTime(nomeTime);
		List<Ingresso> ingressos = new ArrayList<Ingresso>();
		for (Jogo jogo : jogos){
			ingressos.addAll(jogo.getIngressos());
		}
		return ingressos;
	}
}
