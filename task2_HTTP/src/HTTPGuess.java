import java.util.Random;

public class HTTPGuess {
    public int secretNum;
    private int tryTimes;
    private int guessNum;
    public String status;
    public int gameID;


    public HTTPGuess(int gameID){
        secretNum = new Random().nextInt(100);
        tryTimes = 0;
        status = "not started";
        this.gameID = gameID;
    }
    private void updateGuessNum(int guessNum){
        if(guessNum>100||guessNum<0){
            System.out.println("bad number");
        }else {
            this.guessNum = guessNum;
        }
    }
    private void updateTryTimes(){
        tryTimes ++;
    }
    private boolean isCorrect(){
        return secretNum == guessNum;
    }
    private boolean isHigher(){
        return secretNum < guessNum;
    }
    private boolean isLower(){
        return secretNum > guessNum;
    }

    public int getTryTimes(){
        return tryTimes;
    }

    public void play(int guessNum){

        updateGuessNum(guessNum);
        updateTryTimes();
        if (isLower()){
            status = "higher";
        }else
        if (isHigher()){
            status = "lower";
        }else
        if (isCorrect()){
            status = "correct";
        }else {
            status = "error";
        }
    }



}
