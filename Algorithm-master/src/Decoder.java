import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Decoder {

    public static final String fileName = "src/resource/fano.txt";

    public String decodeAlgorithm(){
        Scanner in = new Scanner(System.in);
        String encodedMessage = in.nextLine();
        Map<String, String> code = createCodeLetter();
        return decodeMessage(code, encodedMessage);
    }

    private String decodeMessage(Map<String, String> code, String message){
        StringBuilder decodedMessage = new StringBuilder();
        StringBuilder str = new StringBuilder();
        for (char number : message.toCharArray()){
            str.append(number);
            if (code.containsValue(str.toString())){
                decodedMessage.append(code.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), str.toString())).map(Map.Entry::getKey).findFirst().get());
                str.setLength(0);
            }
        }
        return decodedMessage.toString();
    }

    private Map<String, String> createCodeLetter() {
        Map<String, String> code = new HashMap<>(); // symbol - code
        List<String> alphabet = Arrays.stream(readFromFile(0).split(" ")).collect(Collectors.toList());
        List<Double> probability = Arrays.stream(readFromFile(1).split(" ")).map(Double::parseDouble).collect(Collectors.toList());

        Map<String, Double> alphProb = IntStream.range(0, alphabet.size()).boxed().collect(Collectors.toMap(alphabet::get, probability::get));
        alphProb = alphProb.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        searchTree("", "", 0, alphProb.size() - 1, alphProb, code);
        return code;
    }

    private void searchTree(String branch, String fullBranch, int startPos, int endPos, Map<String, Double> alphProb, Map<String, String> code){
        double dS = 0.0;
        int m;
        double sum = 0.0;
        String cBranch;

        cBranch = fullBranch + branch;

        if (startPos == endPos){
            code.put(getLetterFromPosition(alphProb, startPos), cBranch);
            return;
        }

        for (int i = startPos; i < endPos; i++) {
            dS += getProbFromPosition(alphProb, i);
        }
        dS = dS / 2;

        m = startPos;
        while ((sum + getProbFromPosition(alphProb, m) < dS) & (m < endPos)){
            sum += getProbFromPosition(alphProb, m);
            m++;
        }

        searchTree("0", cBranch, startPos, m, alphProb, code);
        searchTree("1", cBranch, m + 1, endPos, alphProb, code);
    }

    private String readFromFile(int row) {
        try{
            return Files.lines(Paths.get(fileName)).collect(Collectors.toList()).get(row);
        } catch (IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private String getLetterFromPosition(Map<String, Double> alphProb, int pos){
        return alphProb.keySet().toArray()[pos].toString();
    }

    private Double getProbFromPosition(Map<String, Double> alphProb, int pos){
        return Double.parseDouble(alphProb.values().toArray()[pos].toString());
    }
}
