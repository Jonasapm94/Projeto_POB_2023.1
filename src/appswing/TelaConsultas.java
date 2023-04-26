/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;
import modelo.Jogo;
import modelo.Time;
import regras_negocio.Fachada;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class TelaConsultas {
	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel labelMessage;



	/**
	 * Launch the application.
	 */
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					TelaJogo window = new TelaJogo();
	//					window.frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}

	/**
	 * Create the application.
	 */
	public TelaConsultas() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Consultas");
		frame.setBounds(100, 100, 729, 385);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Fachada.inicializar();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				Fachada.finalizar();
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 43, 674, 219);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(Color.WHITE);
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		labelMessage = new JLabel("selecione");
		labelMessage.setBounds(21, 273, 431, 14);
		frame.getContentPane().add(labelMessage);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Quais as datas que um time X joga", "Quantos ingressos INDIVIDUAIS um time X vendeu", "Quantos ingressos em GRUPO um time X vende", "Quais times jogam em determinada data?", "Quais os códigos de ingressos de todos os jogos de um time X"}));
		comboBox.setBounds(21, 11, 539, 22);
		frame.getContentPane().add(comboBox);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.setBounds(606, 11, 89, 23);
		frame.getContentPane().add(btnConsultar);
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = comboBox.getSelectedIndex();
					switch (index) {
						case 0: //Quais as datas dos jogos de um time X.
							try {
								String nomeTime = JOptionPane.showInputDialog("Digite o Nome do Time:");
								List<Jogo>jogos = Fachada.listarJogosDoTime(nomeTime);
								//model contem todas as linhas e colunas da tabela
								DefaultTableModel model = new DefaultTableModel();
								//colunas
								model.addColumn("data");
								model.addColumn("local");
								model.addColumn("time adversario");
								//linhas
								for(Jogo jogo : jogos) {
									if(nomeTime.equals(jogo.getTime1().getNome()))
										model.addRow(new Object[]{jogo.getData(),jogo.getLocal(),jogo.getTime2().getNome()});
									else
										model.addRow(new Object[]{jogo.getData(),jogo.getLocal(),jogo.getTime1().getNome()});
								}
								table.setModel(model);
								
							}catch(Exception ex) {
								labelMessage.setText(ex.getMessage());
							}
							break;
							
						case 1: //Quantos ingressos INDIVIDUAIS um time X vendeu
							try {
								String nomeTime = JOptionPane.showInputDialog("Digite o Nome do Time:");
								List<IngressoIndividual> ingressos = Fachada.numIngressosIndividuaisVendidosPorTime(nomeTime);
								//model contem todas as linhas e colunas da tabela
								DefaultTableModel model = new DefaultTableModel();
								//colunas
								model.addColumn("Quantidade de Ingressos INDIVIDUAIS Vendidos");
								//linhas
								model.addRow(new Object[]{ingressos.size()});
								table.setModel(model);
								
							}catch(Exception ex) {
								labelMessage.setText(ex.getMessage());
							}
							break;
						case 2: //Quantos ingressos em GRUPO um time X vendeu
							try {
								String nomeTime = JOptionPane.showInputDialog("Digite o Nome do Time:");
								List<IngressoGrupo> ingressos = Fachada.numIngressosGrupoVendidosPorTime(nomeTime);
								//model contem todas as linhas e colunas da tabela
								DefaultTableModel model = new DefaultTableModel();
								//colunas
								model.addColumn("Quantidade de Ingressos de GRUPO Vendidos");
								//linhas
								model.addRow(new Object[]{ingressos.size()});
								table.setModel(model);
								
							}catch(Exception ex) {
								labelMessage.setText(ex.getMessage());
							}
							break;
						case 3: //Qual o estoque de ingressos disponíveis para os jogos de um time X
							try {
								String data = JOptionPane.showInputDialog("Digite a data do jogo:");
								List<Time>times = Fachada.timesJogandoPorData(data);
								//model contem todas as linhas e colunas da tabela
								DefaultTableModel model = new DefaultTableModel();
								//colunas
								model.addColumn("Nome do time");
								//linhas
								for(Time time : times) {
									model.addRow(new Object[]{time.getNome()});
								}
								table.setModel(model);
								
							}catch(Exception ex) {
								labelMessage.setText(ex.getMessage());
							}
							break;
						case 4: //Quais os códigos dos ingressos de todos os jogos de um time X
							try {
								String nomeTime = JOptionPane.showInputDialog("Digite o Nome do Time:");
								List<Ingresso>ingressos = Fachada.codigosIngressoPorTime(nomeTime);
								//model contem todas as linhas e colunas da tabela
								DefaultTableModel model = new DefaultTableModel();
								//colunas
								model.addColumn("Tipo do Ingresso");
								model.addColumn("Código do Ingresso");
								//linhas
								for(Ingresso ingresso : ingressos) {
									if(ingresso instanceof IngressoGrupo grupo) {
										model.addRow(new Object[]{"Grupo" ,grupo.getCodigo()});
									}
									else {
										model.addRow(new Object[]{"Individual" ,ingresso.getCodigo()});
									}
								}
								table.setModel(model);
								
							}catch(Exception ex) {
								labelMessage.setText(ex.getMessage());
							}
							break;
						default:
							break;
					}
				}
				catch(Exception ex) {
					labelMessage.setText(ex.getMessage());
				}
			}
		});
	}
}
