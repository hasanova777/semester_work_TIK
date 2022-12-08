import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Coder {

    public static final String fileName = "src/resource/mtf.txt";

    public Map<String, Integer> bwTransform(){
        Scanner in = new Scanner(System.in);
        String message = in.nextLine();

        Set<String> treeSet = new TreeSet<>();
        for (int i = 0; i < message.length(); i++){
            treeSet.add(message);
            message = message.substring(1) + message.charAt(0);
        }
        String encoded = treeSet.stream().map(a -> a.charAt(a.length() - 1)).map(Object::toString).reduce((x, y) -> x + y).get();
        int position = new ArrayList<>(treeSet).indexOf(message);
        return Map.of(encoded, position);
    }

    public Map<String, Integer> mtfEncode(Map<String, Integer> bwtTransformed){
        String message = String.valueOf(bwtTransformed.keySet().toArray()[0]);
        List<String> alphabet = Arrays.stream(readFromFile().split(" ")).collect(Collectors.toList());
        List<String> codeList = IntStream.range(0, alphabet.size()).boxed().map(Object::toString).collect(Collectors.toList());

        StringBuilder output = new StringBuilder();

        for(char letter : message.toCharArray()){
            int ind = alphabet.indexOf(String.valueOf(letter));
            String code = codeList.get(ind);
            output.append(code);
            alphabet.remove(ind);
            alphabet.add(0, String.valueOf(letter));
        }
        return Map.of(output.toString(), Integer.parseInt(bwtTransformed.values().toArray()[0].toString()));
    }

    private String readFromFile() {
        try{
            return Files.lines(Paths.get(fileName)).collect(Collectors.toList()).get(0);
        } catch (IOException e){
            throw new IllegalArgumentException(e);
        }
    }

}
