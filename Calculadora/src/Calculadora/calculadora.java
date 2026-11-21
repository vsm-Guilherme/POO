package Calculadora;

// Credenciais
//RA: 0023448 | Guilherme Venâncio de Sousa Menezes
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class calculadora {

	// Paleta baseada em RGB (130, 107, 206)
	private static final Color COR_PRINCIPAL = new Color(130, 107, 206);
	private static final Color COR_ESCURA    = new Color(92, 73, 158);   // operações
	private static final Color COR_CLARA     = new Color(236, 232, 249); // números
	private static final Color COR_TEXTO_ESC = Color.WHITE;

	// Medidas da grade de botões
	private static final int MARGEM = 20;
	private static final int LARGURA_BTN = 70;
	private static final int ALTURA_BTN = 60;
	private static final int ESPACO = 10;

	private JFrame frame;
	private JTextField JTFResultado;

	// Variáveis de controle da calculadora
	private double valor1 = 0;
	private String op = "";
	private boolean novoNumero = true;

	/**
	 * Abre a aplicação.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					calculadora window = new calculadora();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Criação da aplicação.
	 */
	public calculadora() {
		initialize();
	}

	/**
	 * Cria um botão numa célula da grade (coluna/linha começam em 0).
	 * "colunas" permite um botão ocupar mais de uma coluna.
	 */
	private JButton criarBotao(String texto, int coluna, int linha, int colunas,
			boolean operacao, Runnable acao) {
		JButton botao = new JButton(texto);
		botao.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acao.run();
			}
		});
		botao.setFont(new Font("Arial", Font.BOLD, 18));

		int x = MARGEM + coluna * (LARGURA_BTN + ESPACO);
		int y = 160 + linha * (ALTURA_BTN + ESPACO);
		int largura = colunas * LARGURA_BTN + (colunas - 1) * ESPACO;
		botao.setBounds(x, y, largura, ALTURA_BTN);

		// Cores
		botao.setOpaque(true);
		botao.setBorderPainted(false);
		botao.setFocusPainted(false);
		if (operacao) {
			botao.setBackground(COR_ESCURA);
			botao.setForeground(COR_TEXTO_ESC);
		} else {
			botao.setBackground(COR_CLARA);
			botao.setForeground(COR_ESCURA);
		}

		frame.getContentPane().add(botao);
		return botao;
	}

	/** Botões numéricos: acrescenta o dígito ao que já está no visor. */
	private void digitar(String d) {
		if (novoNumero) {
			JTFResultado.setText(d);
			novoNumero = false;
		} else {
			JTFResultado.setText(JTFResultado.getText() + d);
		}
	}

	/** Botão de ponto/vírgula decimal. */
	private void digitarPonto() {
		if (novoNumero) {
			JTFResultado.setText("0.");
			novoNumero = false;
		} else if (!JTFResultado.getText().contains(".")) {
			JTFResultado.setText(JTFResultado.getText() + ".");
		}
	}

	/** Botões + - * / : guarda o primeiro valor e a operação escolhida. */
	private void escolherOperacao(String novaOp) {
		if (!JTFResultado.getText().isEmpty()) {
			if (!op.isEmpty() && !novoNumero) {
				calcular(); // permite encadear: 2 + 3 + 4
			}
			valor1 = Double.parseDouble(JTFResultado.getText());
		}
		op = novaOp;
		novoNumero = true;
	}

	/** Botão = : faz a conta com valor1, a operação e o valor do visor. */
	private void calcular() {
		if (op.isEmpty() || JTFResultado.getText().isEmpty()) {
			return;
		}
		double valor2 = Double.parseDouble(JTFResultado.getText());
		double resultado = 0;

		if (op.equals("Soma")) {
			resultado = valor1 + valor2;
		} else if (op.equals("Subtracao")) {
			resultado = valor1 - valor2;
		} else if (op.equals("Multiplicacao")) {
			resultado = valor1 * valor2;
		} else if (op.equals("Potencia")) {
			resultado = Math.pow(valor1, valor2);
		} else if (op.equals("Divisao")) {
			if (valor2 == 0) {
				JTFResultado.setText("Erro");
				op = "";
				novoNumero = true;
				return;
			}
			resultado = valor1 / valor2;
		}

		// Mostra sem ".0" quando o resultado é inteiro
		if (resultado == (long) resultado) {
			JTFResultado.setText(String.valueOf((long) resultado));
		} else {
			JTFResultado.setText(String.valueOf(resultado));
		}
		valor1 = resultado;
		op = "";
		novoNumero = true;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Guilherme Venâncio de Sousa Menezes/ 0023448");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(COR_PRINCIPAL);

		// Área útil: 4 colunas de botões + margens; 5 linhas + visor
		int largura = 2 * MARGEM + 4 * LARGURA_BTN + 3 * ESPACO;   // 350
		int altura = 160 + 5 * ALTURA_BTN + 4 * ESPACO + MARGEM;   // 520
		frame.getContentPane().setPreferredSize(new Dimension(largura, altura));
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		// Visor
		JTFResultado = new JTextField();
		JTFResultado.setBounds(MARGEM, MARGEM, largura - 2 * MARGEM, 120);
		JTFResultado.setFont(new Font("Arial", Font.BOLD, 36));
		JTFResultado.setHorizontalAlignment(SwingConstants.RIGHT);
		JTFResultado.setBackground(Color.WHITE);
		JTFResultado.setForeground(COR_ESCURA);
		JTFResultado.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COR_ESCURA, 2),
				BorderFactory.createEmptyBorder(0, 10, 0, 10)));
		frame.getContentPane().add(JTFResultado);
		JTFResultado.setColumns(10);

		// Linha 0:  7  8  9  /
		criarBotao("7", 0, 0, 1, false, () -> digitar("7"));
		criarBotao("8", 1, 0, 1, false, () -> digitar("8"));
		criarBotao("9", 2, 0, 1, false, () -> digitar("9"));
		criarBotao("/", 3, 0, 1, true, () -> escolherOperacao("Divisao"));

		// Linha 1:  4  5  6  *
		criarBotao("4", 0, 1, 1, false, () -> digitar("4"));
		criarBotao("5", 1, 1, 1, false, () -> digitar("5"));
		criarBotao("6", 2, 1, 1, false, () -> digitar("6"));
		criarBotao("*", 3, 1, 1, true, () -> escolherOperacao("Multiplicacao"));

		// Linha 2:  1  2  3  -
		criarBotao("1", 0, 2, 1, false, () -> digitar("1"));
		criarBotao("2", 1, 2, 1, false, () -> digitar("2"));
		criarBotao("3", 2, 2, 1, false, () -> digitar("3"));
		criarBotao("-", 3, 2, 1, true, () -> escolherOperacao("Subtracao"));

		// Linha 3:  0  .  ,  +   (ponto e vírgula fazem a mesma coisa)
		criarBotao("0", 0, 3, 1, false, () -> digitar("0"));
		criarBotao(".", 1, 3, 1, false, () -> digitarPonto());
		criarBotao(",", 2, 3, 1, false, () -> digitarPonto());
		criarBotao("+", 3, 3, 1, true, () -> escolherOperacao("Soma"));

		// Linha 4:  C  ^  =  (= ocupa 2 colunas)
		criarBotao("C", 0, 4, 1, true, () -> {
			JTFResultado.setText("");
			valor1 = 0;
			op = "";
			novoNumero = true;
		});
		criarBotao("^", 1, 4, 1, true, () -> escolherOperacao("Potencia"));
		criarBotao("=", 2, 4, 2, true, () -> calcular());
	}
}
