package com.peli.restpeli;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/")
public class QuizRestController {

    private QuizGame quizGame;

    public QuizRestController() {
        QuizQuestion question1 = new QuizQuestion(
                "1. Kuka on kaikkien aikojen eniten Grammy-palkintoja voittanut mies?",
                List.of("A) Quincy Jones", "B) Stevie Wonder", "C) Michael Jackson"), "B");
        QuizQuestion question2 = new QuizQuestion("2. Kuka on säveltänyt 'Neljä vuodenaikaa'?",
                List.of("A) Wolfgang Amadeus Mozart", "B) Ludwig van Beethoven", "C) Antonio Vivaldi"), "C");
        QuizQuestion question3 = new QuizQuestion("3. Minkä yhtyeen tunnettu albumi on nimeltään 'Rumours'?",
                List.of("A) Eagles", "B) Fleetwood Mac", "C) Rolling Stones"), "B");
        QuizQuestion question4 = new QuizQuestion("4. Kuka on kaikkien aikojen myydyin naisartisti?",
                List.of("A) Madonna", "B) Whitney Houston", "C) Celine Dion"), "A");
        QuizQuestion question5 = new QuizQuestion("5. Kuka on maailman kovin kitaristi?",
                List.of("A) Reima Riihimäki", "B) Matti Nieminen", "C) Jimi Hendrix"), "B");
        this.quizGame = new QuizGame(List.of(question1, question2, question3, question4, question5));
    }

    @GetMapping("/") // Tämä metodi on juuripolku, jossa näytetään ohjeet pelin pelaamiseen.
    public String getInstructions() {
        return "Tervetuloa pelaamaan musavisaa!<br><br> Tässä visassa on viisi kysymystä. Voit siirtyä kysymyksissä haluttuun kysymykseen lähettämällä GET-pyynnön /question/1-5. <br><br>Lähetä GET-pyyntö /question saadaksesi kysymyksen. <br><br>Lähetä POST-pyyntö /answer vastataksesi kysymykseen, käytä vastauksen parametrina 'answer'.";
    }

    @GetMapping("/question") // Tällä metodilla näytetään kysymys.
    public String getQuestionText() {
        QuizQuestion currentQuestion = quizGame.getCurrentQuestion();
        if (currentQuestion != null) {
            return currentQuestion.getQuestionText() + "<br><br>" + "Vaihtoehdot:<br><br>"
                    + String.join(", ", currentQuestion.getOptions()) + ".";
        } else {
            return "Peli on päättynyt. Lähetä GET-pyyntö /reset aloittaaksesi pelin alusta.";
        }
    }

    @GetMapping("/question/{index}") // Tällä metodilla voidaan siirtyä haluttuun kysymykseen indeksin perusteella.
    public String getQuestionTextByIndex(@PathVariable int index) {
        quizGame.setCurrentQuestion((index) - 1);
        QuizQuestion currentQuestion = quizGame.getCurrentQuestion();
        if (currentQuestion != null) {
            return currentQuestion.getQuestionText() + "<br><br>" + "Vaihtoehdot:<br><br>"
                    + String.join(", ", currentQuestion.getOptions()) + ".";
        } else {
            return "Kysymystä ei löytynyt. Käytä indexinä 1-5.";
        }
    }

    @PostMapping("/answer") // Tällä metodilla tarkistetaan vastaus.
    public String checkAnswer(@RequestParam String answer) {
        QuizQuestion currentQuestion = quizGame.getCurrentQuestion();
        if (currentQuestion != null) {
            if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(answer)) {
                if (quizGame.moveToNextQuestion()) {
                    return "Oikea vastaus! Päivitä /question pyyntö, jotta saat seuraavan kysymyksen.";
                } else {
                    return "Oikea vastaus! Kaikkiin kysymyksiin vastattu, peli päättyi. Lähetä GET-pyyntö /reset aloittaaksesi pelin alusta.";
                }
            } else {
                return "Väärä vastaus. Yritä uudelleen.";
            }
        } else {
            return "Peli on päättynyt. Lähetä /reset pyyntö aloittaaksesi uuden pelin.";
        }
    }

    @GetMapping("/reset") // Tällä metodilla nollataan peli.
    public String resetGame() {
        quizGame.resetGame();
        return "Peli on nyt resetoitu. Aloita peli alusta tekemällä GET-pyyntö /question.";
    }
}