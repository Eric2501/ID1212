import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.WeakHashMap;

public class HTTPResponseGenerator {

    String statusOK = "HTTP/1.1 200 OK\r\n";
    String statusNotFound = "HTTP/1.1 404 Not Found\n";
    String header1 = "Content-Type: text/html\r\n";
    String header2 ;
    String header3 ;
    String emptyLine = "\r\n";
    String message = "";
    Integer guessTimes;
    String hint;
    int messageLength;
    String length;
    String cookie;
    String htmlPath = "";
    String responseType = "";
    boolean setCookie = false;
    boolean isOK = true;

    public HTTPResponseGenerator(String responseType, int guessTimes, String hint, int gameIDCookie){

        this.guessTimes = guessTimes;
        this.hint = hint;
        cookie = Integer.toString(gameIDCookie);

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

    public HTTPResponseGenerator(String responseType,int gameIDCookie){


        if (responseType.equals("index")){
            this.responseType = responseType;
            htmlPath = "index.html";
            setCookie = true;
            cookie = Integer.toString(gameIDCookie);
        }else if (responseType.equals("error")){
            this.responseType = responseType;
            isOK = false;
            htmlPath = "404.html";
        }else {
            System.out.println("invalid response type");
        }
    }

    public String readHtml(String path) throws IOException {
        BufferedReader htmlReader = new BufferedReader(new FileReader(path));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = htmlReader.readLine()) != null){

            messageLength = messageLength + line.toCharArray().length;

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
        length = Integer.toString(messageLength);
        header2 = "Content-length: " + length + "\r\n";
        return builder.toString();
    }

    public String construct() throws IOException {
        StringBuilder response = new StringBuilder();
        String[] generalRes;

        if (isOK){
            message = readHtml(htmlPath);
            if (setCookie){
                header3 = "set-cookie: gameid=" + cookie + emptyLine;
                generalRes = new String[]{statusOK, header1, header2, header3, emptyLine};
            }else {
                generalRes = new String[]{statusOK, header1, header2, emptyLine};
            }
        }else {
            message = readHtml(htmlPath);
            generalRes = new String[]{statusNotFound,header1,header2,emptyLine};
        }


        for (String generalRe : generalRes) {
            response.append(generalRe);
        }



        response.append(message);

        return response.toString();

    }

}
