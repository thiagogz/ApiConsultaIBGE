package org.estudos.br;

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
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ConsultaDistritoIBGETest {
    private static final String DISTRITOS_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/distritos/";

    @Mock
    private HttpURLConnection connectionMock;

    private static final String JSON_RESPONSE =
            "[{\"id\":355030859,\"nome\":\"Penha\",\"municipio\":{\"id\":3550308,\"nome\":\"São Paulo\",\"microrregiao\":{\"id\":35061,\"nome\":\"São Paulo\",\"mesorregiao\":{\"id\":3515,\"nome\":\"Metropolitana de São Paulo\",\"UF\":{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}}},\"regiao-imediata\":{\"id\":350001,\"nome\":\"São Paulo\",\"regiao-intermediaria\":{\"id\":3501,\"nome\":\"São Paulo\",\"UF\":{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}}}}}]";

    @BeforeEach
    public void criaMock() throws IOException {
        MockitoAnnotations.openMocks(this); // Inicializando o Mock

        // Configurando o Mock
        InputStream inputStream = new ByteArrayInputStream(JSON_RESPONSE.getBytes());
        when(connectionMock.getInputStream()).thenReturn(inputStream);
    }

    @Test
    @DisplayName("Teste distrito simples")
    public void testConsultarDistrito() throws IOException {
        int idDistrito = 355030859; // Distrito que será consultado
        String respostaEsperada = "[{\"id\":355030859,\"nome\":\"Penha\",\"municipio\":{\"id\":3550308,\"nome\":\"São Paulo\",\"microrregiao\":{\"id\":35061,\"nome\":\"São Paulo\",\"mesorregiao\":{\"id\":3515,\"nome\":\"Metropolitana de São Paulo\",\"UF\":{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}}},\"regiao-imediata\":{\"id\":350001,\"nome\":\"São Paulo\",\"regiao-intermediaria\":{\"id\":3501,\"nome\":\"São Paulo\",\"UF\":{\"id\":35,\"sigla\":\"SP\",\"nome\":\"São Paulo\",\"regiao\":{\"id\":3,\"sigla\":\"SE\",\"nome\":\"Sudeste\"}}}}}}]"; // Resposta esperada
        String resposta = ConsultaIBGE.consultarDistrito(idDistrito); // Chama o método utilizado para teste

        // Assert
        assertEquals(respostaEsperada, resposta);
    }

    @ParameterizedTest
    @CsvSource({"310620005", "355030859", "330455705"})
    @DisplayName("Teste distrito parametrizado")
    public void testConsultarDistritoParametrizado(int distrito) throws IOException {
        int idDistrito = distrito; // Define o distrito a ser consultado
        String resposta = ConsultaIBGE.consultarDistrito(idDistrito); // Chama o método utilizado para teste

        assert !resposta.isEmpty(); // Verifica se a resposta não está vazia

        // Verifica se o status code é 200 (OK)
        HttpURLConnection connection = (HttpURLConnection) new URL(DISTRITOS_API_URL + idDistrito).openConnection();
        int statusCode = connection.getResponseCode();
        assertEquals(200, statusCode, "O status code da resposta da API deve ser 200 (OK)");
    }

    @Test
    @DisplayName("Teste distrito usando o Mock")
    public void testConsultarDistritoComMock() throws IOException {
        int idDistrito = 355030859; // Estado que será consultado
        String resposta = ConsultaIBGE.consultarDistrito(idDistrito); // Chama o método utilizado para teste
        assertEquals(JSON_RESPONSE, resposta, "O JSON retornado não corresponde ao esperado."); // Verifica a resposta e o JSON esperado
    }
}
