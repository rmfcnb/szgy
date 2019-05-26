package hu.elte.szgy.footballapp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.szgy.footballapp.data.Competition;
import hu.elte.szgy.footballapp.data.Team;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FootballappApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private <T> T mapFromJson(String json, Class<T> tClass) throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, tClass);
    }

    @Before
    public void setUp() throws Exception {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
        mockMvc.perform(MockMvcRequestBuilders.get("/competition/generateCompetitions").accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void testCompetitions() throws Exception {
        String uri = "/competition/byname/league";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<Competition> competitions = mapFromJson(content, List.class);
        assertEquals(10, competitions.size());

        uri = "/competition/byname/1";
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        competitions = mapFromJson(content, List.class);
        assertEquals(1, competitions.size());
    }

    @Test
    public void testTeams() throws Exception {
        String uri = "/team/byname/team";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<Team> teams = mapFromJson(content, List.class);
        assertEquals(20, teams.size());

        uri = "/team/byname/1";
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        teams = mapFromJson(content, List.class);
        assertEquals(11, teams.size());
    }

    @Test
    public void testHome() throws Exception{
        String compUri = "/competition/home";
        MvcResult compMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(compUri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int compStatus = compMvcResult.getResponse().getStatus();
        assertEquals(200, compStatus);
        String content = compMvcResult.getResponse().getContentAsString();
        List<Competition> competitions = mapFromJson(content, List.class);
        assertEquals(10, competitions.size());

        String teamUri = "/team/home";
        MvcResult teamMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(teamUri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int teamStatus = teamMvcResult.getResponse().getStatus();
        assertEquals(200, teamStatus);
        String teamContent = teamMvcResult.getResponse().getContentAsString();
        List<Team> teams = mapFromJson(teamContent, List.class);
        assertEquals(10, teams.size());
    }
}
