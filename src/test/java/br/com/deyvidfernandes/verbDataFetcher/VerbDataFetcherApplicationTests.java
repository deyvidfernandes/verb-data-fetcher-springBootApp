package br.com.deyvidfernandes.verbDataFetcher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class VerbDataFetcherApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnCreateWhenConnectionIsEstablished() throws Exception{
		this.mockMvc.perform(post("/database/")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"url\": \"localhost:3306/test\", \"type\": \"MYSQL\", \"username\": \"testUser\", \"password\": \"1234\", \"table\": \"testverbs\"}"))
				.andDo(print())
				.andExpect(status()
				.isCreated());
	}



}
