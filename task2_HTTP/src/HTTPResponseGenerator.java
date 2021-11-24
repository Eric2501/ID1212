import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HTTPResponseGenerator {

    String statusOK = "HTTP/1.1 200 OK\r\n";
    String header1 = "Content-Type: text/html\r\n";
    String header2 = "";
    String emptyLine = "\r\n";
    String message = "";
    Integer guessTimes;
    String hint;

    String htmlPath = "";
    String responseType = "";

    public HTTPResponseGenerator(String responseType, int guessTimes, String hint){

        this.guessTimes = guessTimes;
        this.hint = hint;

        if (responseType != null){
            this.responseType = responseType;

            switch (this.responseType) {
                case "index" -> htmlPath = "index.html";
                case "correct" -> htmlPath = "correct.html";
                case "wrong" -> htmlPath = "wrong.html";
            }
        }else {
            System.out.println("invalid response type");
        }

    }

    public String readHtml(String path) throws IOException {
        BufferedReader htmlReader = new BufferedReader(new FileReader(path));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = htmlReader.readLine()) != null){

            if (responseType.equals("correct")){
                if (line.contains("*")){
                    line = "You made it in " + guessTimes + " guess(es).";
                }
            }

            if (responseType.equals("wrong")){
                if (line.contains("*")){
                    line = "Nope, guess " + hint + ", You have made " + guessTimes.toString() +  " guess(es)";
                }
            }

            builder.append(line);
        }
        return builder.toString();
    }

    public String construct() throws IOException {
        StringBuilder response = new StringBuilder();

        String[] generalRes = {statusOK,header1,emptyLine};
        for (int i = 0; i < generalRes.length; i ++){
            response.append(generalRes[i]);
        }

        message = readHtml(htmlPath);

        response.append(message);

        return response.toString();

    }

}
