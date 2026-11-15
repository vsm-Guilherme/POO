package crudnuvem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.Scanner;

public class produto {

    public static void main(String[] args) {

        // CONEXAO COM O RAILWAY
        String url = "Removido po Segurança";
        String usuariobanco = "root";
        String bancosenha = "Removido por Segurança";

        Scanner sc = new Scanner(System.in);

        try (Connection conexao = DriverManager.getConnection(
                url, usuariobanco, bancosenha)) {

            System.out.println("Conectado ao banco com sucesso!");

            // CRIAR TABELA CASO NAO EXISTA
            String criarTabela = "CREATE TABLE IF NOT EXISTS PRODUTO ("
                    + "CODIGO INT PRIMARY KEY, "
                    + "NOME VARCHAR(100) NOT NULL, "
                    + "PRECO DECIMAL(10,2) NOT NULL)";

            try (Statement criar = conexao.createStatement()) {
                criar.executeUpdate(criarTabela);
            }

            int opcao = -1;

            while (opcao != 0) {

                System.out.println("\n===== MENU PRODUTOS =====");
                System.out.println("1 - Inserir");
                System.out.println("2 - Listar");
                System.out.println("3 - Atualizar");
                System.out.println("4 - Deletar");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opcao: ");

                opcao = Integer.parseInt(sc.nextLine());

                try {

                    switch (opcao) {

                    case 1:
                        // INSERIR PRODUTO
                        System.out.print("Codigo do produto: ");
                        int codigo = Integer.parseInt(sc.nextLine());

                        System.out.print("Nome do produto: ");
                        String nome = sc.nextLine();

                        System.out.print("Preco do produto: ");
                        BigDecimal preco = new BigDecimal(
                                sc.nextLine().replace(",", "."));

                        String sql = "INSERT INTO PRODUTO "
                                + "(CODIGO, NOME, PRECO) VALUES (?, ?, ?)";

                        try (PreparedStatement stmt =
                                conexao.prepareStatement(sql)) {

                            stmt.setInt(1, codigo);
                            stmt.setString(2, nome);
                            stmt.setBigDecimal(3, preco);

                            stmt.executeUpdate();

                            System.out.println("Inserido com sucesso!");
                        }
                        break;

                    case 2:
                        // LISTAR PRODUTOS
                        String sqlListar = "SELECT * FROM PRODUTO ORDER BY CODIGO";

                        try (PreparedStatement stmt =
                                conexao.prepareStatement(sqlListar);
                             ResultSet resultado = stmt.executeQuery()) {

                            System.out.println("\n===== LISTA DE PRODUTOS =====");

                            boolean encontrou = false;

                            while (resultado.next()) {
                                encontrou = true;

                                System.out.println(
                                        "Codigo: " + resultado.getInt("CODIGO")
                                        + " | Nome: " + resultado.getString("NOME")
                                        + " | Preco: R$ " + resultado.getBigDecimal("PRECO"));
                            }

                            if (!encontrou) {
                                System.out.println("Nenhum produto cadastrado.");
                            }
                        }
                        break;

                    case 3:
                        // ATUALIZAR PRODUTO
                        System.out.print("Codigo do produto que deseja atualizar: ");
                        int codigoAtualizar = Integer.parseInt(sc.nextLine());

                        System.out.print("Novo nome: ");
                        String novoNome = sc.nextLine();

                        System.out.print("Novo preco: ");
                        BigDecimal novoPreco = new BigDecimal(
                                sc.nextLine().replace(",", "."));

                        String sqlAtualizar =
                                "UPDATE PRODUTO SET NOME = ?, PRECO = ? WHERE CODIGO = ?";

                        try (PreparedStatement stmt =
                                conexao.prepareStatement(sqlAtualizar)) {

                            stmt.setString(1, novoNome);
                            stmt.setBigDecimal(2, novoPreco);
                            stmt.setInt(3, codigoAtualizar);

                            int linhas = stmt.executeUpdate();

                            if (linhas > 0) {
                                System.out.println("Produto atualizado com sucesso!");
                            } else {
                                System.out.println("Produto nao encontrado!");
                            }
                        }
                        break;

                    case 4:
                        // DELETAR PRODUTO
                        System.out.print("Codigo do produto que deseja deletar: ");
                        int codigoDeletar = Integer.parseInt(sc.nextLine());

                        String sqlDeletar =
                                "DELETE FROM PRODUTO WHERE CODIGO = ?";

                        try (PreparedStatement stmt =
                                conexao.prepareStatement(sqlDeletar)) {

                            stmt.setInt(1, codigoDeletar);

                            int linhas = stmt.executeUpdate();

                            if (linhas > 0) {
                                System.out.println("Produto deletado com sucesso!");
                            } else {
                                System.out.println("Produto nao encontrado!");
                            }
                        }
                        break;

                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;

                    default:
                        System.out.println("Opcao invalida!");
                        break;
                    }

                } catch (SQLException | IllegalArgumentException e) {
                    System.out.println("Erro na operacao: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}