package fr.ensim.interop.introrest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;


@RestController
public class JokeRestController {


        private List<Joke> jokes;

        public JokeRestController() throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ClassPathResource classPathResource = new ClassPathResource("Joke.json");
                InputStream inputStream = classPathResource.getInputStream();
                JsonNode jsonNode = objectMapper.readTree(inputStream);
                JsonNode jokeNode = jsonNode.get("joke");
                jokes = objectMapper.convertValue(jokeNode, new TypeReference<List<Joke>>() {
                });
            } catch (IOException e) {
                System.err.println("Error reading Joke.json file: " + e.getMessage());
            }



            if (jokes != null && !jokes.isEmpty()) {
                for (Joke joke : jokes) {
                    System.out.println(joke.getQuestion());
                    System.out.println(joke.getAnswer());
                }
            }
        }

        @GetMapping("/joke")
        public ResponseEntity<Joke> getJoke() {
            if (jokes != null && !jokes.isEmpty()) {
                Random random = new Random();
                int index = random.nextInt(jokes.size());
                Joke joke = jokes.get(index);
                return new ResponseEntity<>(joke, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

