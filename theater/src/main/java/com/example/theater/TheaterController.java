package com.example.theater;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by franciswalsh on 8/2/17.
 */
@Controller
public class TheaterController {

    static final String API_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=be2a38521a7859c95e2d73c48786e4bb";

    public static List<Movie> getMovies(){

        RestTemplate restTemplate = new RestTemplate();
        List<Movie> moviesPlaying = restTemplate.getForObject(API_URL, ResultsPage.class).getResults();
        System.out.println(moviesPlaying);
        return moviesPlaying;
    }

    @RequestMapping("/")
    public String getHomePage(Model model){
        return "home";
    }

    @RequestMapping("/now-playing")
    public String getNowPlayingPage(Model model){
        List<Movie> nowPlayingMovies = getMovies();
        model.addAttribute("nowPlayingMovies", nowPlayingMovies);

        return "now-playing";
    }

    @RequestMapping("/medium-popular-long-name")
    public String getMediumPopularLongNamePage(Model model){
        List<Movie> nowPlayingMovies = getMovies();
        List<Movie> mediumPopularLongNameMovies = new ArrayList<>();
        for (Movie movie : nowPlayingMovies){
            if (movie.getPopularity() >= 30 && movie.getPopularity() <= 80 && movie.getTitle().length() >= 10){
                mediumPopularLongNameMovies.add(movie);
            }
        }
        model.addAttribute("mediumPopularLongNameMovies", mediumPopularLongNameMovies);
        return "medium-popular-long-name";
    }

    @RequestMapping("/overview-mashup")
    public String getOverviewMashupPage(Model model){
        String mashupString = "";
        List<Integer> preRandomIntegers = new ArrayList<>();
        Set<Integer> randomIntegers = new HashSet<>();
        List<String> randomSentences = new ArrayList<>();
        List<Movie> nowPlayingMovies = getMovies();
        List<Movie> fiveMovies = new ArrayList<>();
        for (int i = 0; i < nowPlayingMovies.size(); i++){
            preRandomIntegers.add(i);
        }
        Random random = new Random();
        Collections.shuffle(preRandomIntegers);
        while(randomIntegers.size() <= 5) {
            for (int i : preRandomIntegers) {
                if (Math.random() >= (5 / (preRandomIntegers.size()))) {
                    randomIntegers.add(i);
                }
            }
        }
        for (int i : randomIntegers){
            fiveMovies.add(nowPlayingMovies.get(i));
        }
        for (Movie movie : fiveMovies){
            String[] initialRandomSentences = movie.getOverview().split("//.");
            mashupString += (initialRandomSentences[random.nextInt(initialRandomSentences.length)] + " ");
        }

        model.addAttribute("mashupString", mashupString);
        return "overview-mashup";
    }
}
