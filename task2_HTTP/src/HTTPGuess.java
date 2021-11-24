import java.util.Random;

public class HTTPGuess {
    private int secretNum;
    private int tryTimes;
    private int guessNum;

    public HTTPGuess(){
        secretNum = new Random().nextInt(100);
        tryTimes = 0;
    }
    public void updateGuessNum(int guessNum){
        if(guessNum>100||guessNum<0){
            System.out.println("bad number");
        }else {
            this.guessNum = guessNum;
        }
    }
    public void updateTryTimes(){
        tryTimes ++;
    }
    public boolean isCorrect(){
        return secretNum == guessNum;
    }
    public boolean isHigher(){
        return secretNum < guessNum;
    }
    public boolean isLower(){
        return secretNum > guessNum;
    }

    public int play(int newNum){
        updateGuessNum(newNum);

        if(isCorrect()){
            return 0;
        }else
        if(isHigher()){
            updateTryTimes();
            return 1;
        }else
        if(isLower()){
            updateTryTimes();
            return 2;
        }
        return -1;
    }

}
