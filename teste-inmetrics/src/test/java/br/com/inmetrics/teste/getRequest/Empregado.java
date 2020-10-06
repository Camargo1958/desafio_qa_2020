package br.com.inmetrics.teste.getRequest;

public class Empregado {

        public Integer empregadoId;
        public String nome;
        public String sexo;
        public String cpf;
        public int departamentoId;
        public String cargo;
        public String admissao;
        public String salario;
        public String comissao;
        public String tipoContratacao;

        public Acesso acesso;

}

class Acesso {
        public int acessoId;
        public String email;
        public String password;
}

