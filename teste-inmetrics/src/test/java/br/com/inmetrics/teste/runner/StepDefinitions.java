package br.com.inmetrics.teste.runner;

import br.com.inmetrics.teste.getRequest.Empregado;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import br.com.inmetrics.teste.getRequest.GetData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class StepDefinitions {

    private GetData getData;
    private Response response;
    private int resp_code;
    private String data;
    RequestSpecification httpRequest0;
    public Empregado newEmp = new Empregado();
    public String outJson;
    public List<Empregado> empregados;

    public void populateEmpList(){
        // Transforma a array de objetos Json de empregados em uma lista de objetos da classe Empregado
        ObjectMapper mapper = new ObjectMapper();
        String json = this.data;

        try {
            empregados  = Arrays.asList(mapper.readValue(json, Empregado[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getAllEmpList(){

        this.getData = new GetData();
        this.data = getData.getResponse3("empregado/list_all");

        this.resp_code = this.getData.getResponseCode();
        System.out.println("getAllEmpList - ret code = "+this.resp_code);
        this.populateEmpList();
    }

    public Empregado getEmpDataById(int id) {
        Empregado x = new Empregado();
        for(Empregado empregado: this.empregados)
        {
            if(empregado.empregadoId == id)
                x = empregado;
        }
        return x;
    }

    public Empregado getEmpDataByName(String name) {
        Empregado x = new Empregado();
        for(Empregado empregado: this.empregados)
        {
            if((empregado.nome).equals(name)) {
                x = empregado;
            }
        }
        return x;
    }

    public String prepareOutJson(Empregado emp) {
        // Preapara Json para envio ao servidor
        JSONObject obj = new JSONObject();
        obj.put("nome", emp.nome);
        obj.put("sexo",emp.sexo);
        obj.put("cpf",emp.cpf);
        obj.put("cargo",emp.cargo);
        obj.put("admissao",emp.admissao);
        obj.put("salario",emp.salario);
        obj.put("comissao",emp.comissao);
        obj.put("tipoContratacao",emp.tipoContratacao);
        obj.put("departamentoId",emp.departamentoId);

        StringWriter out = new StringWriter();
        try {
            obj.writeJSONString(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonText = out.toString();
        return jsonText;
    }

    public String prepareUpdateJson(Empregado emp) {
        // Preapara Json com atualização de dados para envio ao servidor
        JSONObject obj = new JSONObject();
        obj.put("admissao",emp.admissao);
        obj.put("cargo",emp.cargo);
        obj.put("comissao",emp.comissao);
        obj.put("cpf",emp.cpf);
        obj.put("departamentoId",emp.departamentoId);
        obj.put("nome", emp.nome);
        obj.put("salario",emp.salario);
        obj.put("sexo",emp.sexo);
        obj.put("tipoContratacao",emp.tipoContratacao);

        StringWriter out = new StringWriter();
        try {
            obj.writeJSONString(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonText = out.toString();
        return jsonText;
    }

    @Given("Uma requisição da lista de todos os empregados foi enviada")
    public void uma_requisição_da_lista_de_todos_os_empregados_foi_enviada() {
        this.getData = new GetData();
        this.data = getData.getResponse3("empregado/list_all");
    }

    @When("Uma resposta retornar sem erro")
    public void uma_resposta_retornar_sem_erro() {
        this.resp_code = this.getData.getResponseCode();
        assertEquals(200,this.resp_code);
    }

    @Then("Teremos uma lista de todos os empregados")
    public void teremos_uma_lista_de_todos_os_empregados() {
        // Registra no log de sistema como evidência
        System.out.println(this.data);
        assertTrue(this.data!="");
    }

    @Given("Uma requisição pelos dados do empregado id {int} foi enviada")
    public void uma_requisição_pelos_dados_do_empregado_id_foi_enviada(int indx) {
        this.getData = new GetData();
        this.data = getData.getResponse3("empregado/list_all");

        this.populateEmpList();

        Empregado emp = getEmpDataById(indx);
        int idEmp = emp.empregadoId;

        System.out.println("idEmp :"+idEmp+"\n");

        assertTrue(indx==idEmp);
    }

    @Then("Teremos os dados do empregado id {int}")
    public void teremos_os_dados_do_empregado_id(Integer indx) {

        Empregado emp = getEmpDataById(indx);
        int idEmp = emp.empregadoId;
        String nomeEmp = emp.nome;
        String sexoEmp = emp.sexo;
        String cpfEmp = emp.cpf;

        System.out.println("idEmp :"+idEmp+"\n");
        System.out.println("nome :"+nomeEmp+"\n");
        System.out.println("sexo :"+sexoEmp+"\n");
        System.out.println("cpf :"+cpfEmp+"\n");

        assertTrue(indx==idEmp);
    }

    @Given("Eu atribua {string} como {string} do novo empregado")
    public void eu_atribua_como_do_empregado(String value, String fldName)  {

        switch (fldName) {

            case "nome":
                this.newEmp.nome = value;
                break;
            case "data de adimissão":
                this.newEmp.admissao = value;
                break;
            case "cargo":
                this.newEmp.cargo = value;
                break;
            case "comissão":
                this.newEmp.comissao = value;
                break;
            case "CPF":
                this.newEmp.cpf = value;
                break;
            case "Departamento":
                this.newEmp.departamentoId = (fldName=="QTS")? 1:99;
                break;
            case "salário":
                this.newEmp.salario = value;
                break;
            case "sexo":
                switch (value){
                    case "Masculino": this.newEmp.sexo = "m";
                        break;
                    case "Feminino": this.newEmp.sexo = "f";
                        break;
                    default: this.newEmp.sexo = "i";
                    break;
                }
                break;
            case "Tipo de contratação":
                this.newEmp.tipoContratacao = value.toLowerCase();
                break;
            default: break;
        }

        assertTrue(true);

    }

    @When("Os dados forem eviados e o sistema aceitar confirmando com código {int}")
    public void os_dados_forem_eviados_e_o_sistema_aceitar_confirmando_com_código(int expectedCode) {
        this.outJson = prepareOutJson(newEmp);
        this.resp_code = 0;

/*  // Não funcionou
        try {
            response = getData.sendEmpData(
                    outJson,
                    "empregado/cadastrar"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

        // Usando alternativa deselegante de código local
        RestAssured.baseURI = "http://inm-api-test.herokuapp.com/";

        RequestSpecification httpRequest0 = RestAssured.given();

        System.out.println("Dados : "+this.outJson);

        try {
            response = httpRequest0
                    .given().header("Content-Type", "application/json")
                    .given().header("Authorization", "Basic aW5tZXRyaWNzOmF1dG9tYWNhbw==")
                    .given().body(this.outJson)
                    .request(Method.POST,"empregado/cadastrar");

            this.resp_code = response.statusCode();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseBody = response.getBody().asString();

        System.out.println("retCode ="+resp_code+"\n");
        System.out.println("Response : "+responseBody+"\n");

        assertEquals(expectedCode,this.resp_code);
    }

    @Then("Verificamos que os dados do empregado {string} foram gravados no banco de dados")
    public void verificamos_que_os_dados_foram_gravados_no_banco_de_dados(String empName) {

        getAllEmpList();

        newEmp = getEmpDataByName(empName);
        int idEmp = newEmp.empregadoId;
        String nomeEmp = newEmp.nome;
        String sexoEmp = newEmp.sexo;
        String cpfEmp = newEmp.cpf;

        System.out.println("idEmp :"+idEmp+"\n");
        System.out.println("nome :"+nomeEmp+"\n");
        System.out.println("sexo :"+sexoEmp+"\n");
        System.out.println("cpf :"+cpfEmp+"\n");

        assertTrue((empName).equals(nomeEmp));
    }

    @Given("Eu obtenha os dados empregado com Id {int}")
    public void eu_obtenha_os_dados_empregado_com_id(Integer indx) {

        getAllEmpList();

        newEmp = getEmpDataById(indx);
        int idEmp = newEmp.empregadoId;
        String nomeEmp = newEmp.nome;
        String sexoEmp = newEmp.sexo;
        String cpfEmp = newEmp.cpf;

        // Log de confirmação
        System.out.println("idEmp :"+idEmp+"\n");
        System.out.println("nome :"+nomeEmp+"\n");
        System.out.println("sexo :"+sexoEmp+"\n");
        System.out.println("cpf :"+cpfEmp+"\n");

        assertTrue(indx==idEmp);

    }

    @Given("Eu atribua {string} como o novo valor de {string} do empregado")
    public void eu_atribua_como_o_novo_valor_de_do_empregado(String newValue, String fldName) {
        eu_atribua_como_do_empregado(newValue, fldName);
        assertTrue(true);
    }

    @When("Os dados forem atualizados no sistema para o empregado Id {int}")
    public void os_dados_forem_atualizados_no_sistema(int id) {

        this.outJson = prepareOutJson(newEmp);
        int expectedCode = 200;
        String strId = Integer.toString(id);
        System.out.println("Id sendo alterado: "+strId);
        System.out.println("path: "+"empregado/alterar/"+strId);

        RestAssured.baseURI = "http://inm-api-test.herokuapp.com/";

        RequestSpecification httpRequest0 = RestAssured.given();

        System.out.println("Dados enviados: "+this.outJson);

        try {
            response = httpRequest0
                    .given().header("Content-Type", "application/json")
                    .given().header("Authorization", "Basic aW5tZXRyaWNzOmF1dG9tYWNhbw==")
                    .given().body(this.outJson)
                    .request(Method.PUT,"empregado/alterar/"+strId);

            this.resp_code = response.statusCode();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseBody = response.getBody().asString();

        System.out.println("retCode ="+resp_code+"\n");
        System.out.println("Response : "+responseBody+"\n");

        assertEquals(expectedCode,this.resp_code);

    }

    @Then("Verificamos que os dados do empregado ID {int} foram gravados no banco de dados")
    public void verificamos_que_os_dados_do_empregado_id_foram_gravados_no_banco_de_dados(int Id) {

            getAllEmpList();

            newEmp = getEmpDataById(Id);
            int idEmp = newEmp.empregadoId;
            String nomeEmp = newEmp.nome;
            String sexoEmp = newEmp.sexo;
            String cpfEmp = newEmp.cpf;

            System.out.println("idEmp :"+idEmp+"\n");
            System.out.println("nome :"+nomeEmp+"\n");
            System.out.println("sexo :"+sexoEmp+"\n");
            System.out.println("cpf :"+cpfEmp+"\n");

            assertTrue(Id == idEmp);

    }
}