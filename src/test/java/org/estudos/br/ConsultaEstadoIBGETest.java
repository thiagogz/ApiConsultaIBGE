package org.estudos.br;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ConsultaEstadoIBGETest {
    @Test
    @DisplayName("Teste estado simples")
    public void testConsultarEstado() throws IOException {
        String uf = "SP"; // Estado que será consultado
        String respostaEsperada = "{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}"; // Resposta esperada
        String resposta = ConsultaIBGE.consultarEstado(uf); // Chama o método utilizado para teste

        // Assert
        assertEquals(respostaEsperada, resposta);
    }

    @ParameterizedTest
    @CsvSource({"SP, SP", "RJ, RJ", "MG, MG"})
    @DisplayName("Teste estado parametrizado")
    public void testConsultarEstadoParametrizado(String estadoEscolhido, String resultadoEsperado) throws IOException {
        String resposta = ConsultaIBGE.consultarEstado(estadoEscolhido); // Chama o método utilizado para teste
        JSONObject jsonResposta = new JSONObject(resposta); // Convertendo a resposta para JSON para facilitar a comparação

        assertEquals(resultadoEsperado, jsonResposta.getString("sigla"));
    }

    @Mock
    private HttpURLConnection connectionMock;

    private static final String JSON_RESPONSE =
            "{\"id\":31,\"sigla\":\"MG\",\"nome\":\"Minas Gerais\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}";

    @BeforeEach
    public void criaMock() throws IOException {
        MockitoAnnotations.openMocks(this); // Inicializando o Mock

        // Configurando o Mock
        InputStream inputStream = new ByteArrayInputStream(JSON_RESPONSE.getBytes());
        when(connectionMock.getInputStream()).thenReturn(inputStream);
    }

    @Test
    @DisplayName("Teste usando o Mock")
    public void testConsultarEstadoComMock() throws IOException {
        String uf = "MG"; // Estado que será consultado
        String resposta = ConsultaIBGE.consultarEstado(uf); // Chama o método utilizado para teste
        assertEquals(JSON_RESPONSE, resposta, "O JSON retornado não corresponde ao esperado."); // Verifica a resposta e o JSON esperado
    }
}
