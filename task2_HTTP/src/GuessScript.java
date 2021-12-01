import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class GuessScript {

    String cookie;
    int count = 0;
    ArrayList<Integer> countList = new ArrayList<>();

    public void requestIndex() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest iniRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/")).build();
        HttpResponse<String> response = null;
        response = client.send(iniRequest, HttpResponse.BodyHandlers.ofString());
        this.cookie = response.headers().allValues("set-cookie").get(0);

    }

    public String doGuess(int guessNum) throws IOException, InterruptedException {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/?guessing=" + guessNum)).header("Cookie",cookie) .build();
        HttpClient client = HttpClient.newHttpClient();
        response = client.send(request,HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        count ++;
        return result;
    }

    public void startGame() throws IOException, InterruptedException {

        int guess = 50;
        int upperBoundary = 100;
        int lowerBoundary = 0;
        String result;

        try {
            requestIndex();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        result = doGuess(guess);


        while (!result.contains("Press button to try again.")){

            if (result.contains("higher")){
                lowerBoundary = guess;
                guess = (upperBoundary + guess)/2;
                result = doGuess(guess);

            }else
            if (result.contains("lower")){
                upperBoundary = guess;
                guess  = (lowerBoundary + guess)/2;
                result = doGuess(guess);
            }
        }
        countList.add(count);
        count = 0;
    }

    public int getAverageGuessTimes(){
        int total = 0;
        for (int count:countList){
            total = count + total;
        }
        return total/countList.size();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        GuessScript guessScript = new GuessScript();

        for (int i=0; i < 100; i++){
            guessScript.startGame();
        }
        System.out.println(guessScript.countList);
        System.out.println("The average number of guesses is:");
        System.out.println(guessScript.getAverageGuessTimes());

    }
}
