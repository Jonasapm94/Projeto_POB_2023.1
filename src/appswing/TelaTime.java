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
import javax.swing.JTextField;

public class TelaTime {
	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel label;
	private JLabel label_6;
	private JLabel lblOrigem;
	private JButton button;
	private JButton btnCriarTime;
	private JLabel lblNomeDoTime;
	private JButton btnCancelarTime;
	private JTextField textFieldNome;
	private JTextField textFieldOrigem;



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
	public TelaTime() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Times");
		frame.setBounds(100, 100, 729, 385);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Fachada.inicializar();
				listagem();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				Fachada.finalizar();
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 43, 674, 148);
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

		label = new JLabel("");
		label.setForeground(Color.BLUE);
		label.setBounds(21, 321, 688, 14);
		frame.getContentPane().add(label);

		label_6 = new JLabel("selecione");
		label_6.setBounds(21, 190, 431, 14);
		frame.getContentPane().add(label_6);

		lblOrigem = new JLabel("Origem: ");
		lblOrigem.setHorizontalAlignment(SwingConstants.LEFT);
		lblOrigem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOrigem.setBounds(267, 261, 47, 14);
		frame.getContentPane().add(lblOrigem);

		btnCriarTime = new JButton("Criar Time");
		btnCriarTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = textFieldNome.getText();
					String origem = textFieldOrigem.getText();
					Time time = Fachada.criarTime(nome, origem);
					label.setText("Time " + time.getNome() + " criado com sucesso.");
					listagem();
				}catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		btnCriarTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCriarTime.setBounds(21, 241, 201, 23);
		frame.getContentPane().add(btnCriarTime);

		button = new JButton("Listar");
		button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listagem();
			}
		});
		button.setBounds(267, 9, 89, 23);
		frame.getContentPane().add(button);

		lblNomeDoTime = new JLabel("Nome:");
		lblNomeDoTime.setHorizontalAlignment(SwingConstants.LEFT);
		lblNomeDoTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNomeDoTime.setBounds(267, 231, 47, 14);
		frame.getContentPane().add(lblNomeDoTime);

		btnCancelarTime = new JButton("Excluir Time");
		btnCancelarTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if (table.getSelectedRow() >= 0){
						String nome = (String) table.getValueAt( table.getSelectedRow(), 0);
						Fachada.apagarTime(nome);
						label.setText("Excluido time: " +nome);
						listagem();
					}
					else
						label.setText("Time nao selecionado");
				}
				catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		btnCancelarTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancelarTime.setBounds(428, 9, 145, 23);
		frame.getContentPane().add(btnCancelarTime);
		
		textFieldNome = new JTextField();
		textFieldNome.setBounds(324, 229, 257, 20);
		frame.getContentPane().add(textFieldNome);
		textFieldNome.setColumns(10);
		
		textFieldOrigem = new JTextField();
		textFieldOrigem.setBounds(324, 259, 257, 20);
		frame.getContentPane().add(textFieldOrigem);
		textFieldOrigem.setColumns(10);
	}

	public void listagem() {
		try{
			List<Time> lista = Fachada.listarTimes();

			//model contem todas as linhas e colunas da tabela
			DefaultTableModel model = new DefaultTableModel();

			//colunas
			model.addColumn("Nome");
			model.addColumn("Origem");
			model.addColumn("Jogos");

			//linhas
			String texto;
			for(Time time : lista) {
				texto="";
				for(Jogo j : time.getJogos()){ //obter os id  dos jogos
					texto += j.getId()+ "," ;
				} 								
				model.addRow(new Object[]{time.getNome() , time.getOrigem(), texto});
			}
			table.setModel(model);
			label_6.setText("resultados: "+lista.size()+ " Times  - Selecione uma linha");
		}
		catch(Exception erro){
			label.setText(erro.getMessage());
		}
	}
}
