import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Coder coder = new Coder();

        Map<String, Integer> bwtTransformed = coder.bwTransform();
        System.out.printf("BWT transform: %s, %d\n", bwtTransformed.keySet().toArray()[0],
                Integer.parseInt(bwtTransformed.values().toArray()[0].toString()));

        Map<String, Integer> mtfEncoded = coder.mtfEncode(bwtTransformed);
        System.out.printf("move-to-front encoding: %s\n", mtfEncoded.keySet().toArray()[0]);

        Decoder decoder = new Decoder();

        String fanoDecoded = decoder.decodeAlgorithm();
        System.out.printf("fano decoding: %s\n", fanoDecoded);
    }
}
