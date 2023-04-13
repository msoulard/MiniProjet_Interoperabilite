package fr.ensim.interop.introrest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class JokeRestController {
    private List<Joke> jokes;

    public JokeRestController() throws IOException {
        File file = ResourceUtils.getFile("Joke.json");
        ObjectMapper objectMapper = new ObjectMapper();
        jokes = objectMapper.readValue(file, new TypeReference<List<Joke>>(){});
        for (Joke joke : jokes) {
            System.out.println(joke.getJoke());
            System.out.println(joke.getAnswer());
        }
    }

    @GetMapping("/joke")
    public Joke getJoke() {
        if (jokes != null && !jokes.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(jokes.size());
            return jokes.get(index);
        }
        return null;
    }

}
