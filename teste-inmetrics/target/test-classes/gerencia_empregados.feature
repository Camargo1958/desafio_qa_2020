Feature: Gerencia empregados

  Scenario: Obtem a lista de todos os empregados
    Given Uma requisição da lista de todos os empregados foi enviada
    When Uma resposta retornar sem erro
    Then Teremos uma lista de todos os empregados

  Scenario: Obtem os dados de empregado específico
    Given Uma requisição pelos dados do empregado id 404 foi enviada
    When Uma resposta retornar sem erro
    Then Teremos os dados do empregado id 404

  Scenario: Cadastrar novo empregado
    Given Eu atribua "Johny Silva" como "nome" do novo empregado
    And Eu atribua "22/06/2018" como "data de adimissão" do novo empregado
    And Eu atribua "QA Tester" como "cargo" do novo empregado
    And Eu atribua "500,00" como "comissão" do novo empregado
    And Eu atribua "999.999.999-99" como "CPF" do novo empregado
    And Eu atribua "QTS" como "Departamento" do novo empregado
    And Eu atribua "3.500,00" como "salário" do novo empregado
    And Eu atribua "Masculino" como "sexo" do novo empregado
    And Eu atribua "CLT" como "Tipo de contratação" do novo empregado
    When Os dados forem eviados e o sistema aceitar confirmando com código 202
    Then Verificamos que os dados do empregado "Johny Silva" foram gravados no banco de dados

  Scenario: Alterar os dados de um empregado
    Given Eu obtenha os dados empregado com Id 4087
    And Eu atribua "3.500,00" como o novo valor de "salário" do empregado
    When Os dados forem atualizados no sistema para o empregado Id 4087
   Then Verificamos que os dados do empregado ID 4087 foram gravados no banco de dados