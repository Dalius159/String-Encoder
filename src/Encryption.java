import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Encryption {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.print("Nhap vao mot chuoi ky tu: ");
        String input = scanner.nextLine();
        Map<Character, Integer> frequencies = getCharFrequencies(input);

        Node HuffmanRoot = buildHuffmanTree(frequencies);
        Map<Character, String> HuffmanCodes = genCharCodes(HuffmanRoot);
        String HuffmanEncrypted = encrypt(input, HuffmanCodes);
        System.out.println("Huffman Code: " + HuffmanEncrypted);

        Node ShanonRoot = buildShanonTree(frequencies);
        Map<Character, String> ShanonCodes = genCharCodes(ShanonRoot);
        String ShanonEncrypted = encrypt(input, ShanonCodes);
        System.out.println("Shanon-Fano Code = " + ShanonEncrypted);
        double n = getEfficiency(frequencies, input.length(), ShanonCodes);
        System.out.print("Hieu suat ma hoa = " + n + "\nTinh du thua = " + (1-n));
    }
    
    //Hàm đếm tần suất suất hiện của từng ký tự trong sâu
    private static Map<Character, Integer> getCharFrequencies(String input) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }
        return frequencies;
    }

    //Hàm tính hiệu suất mã hóa
    private static Double getEfficiency(Map<Character, Integer> frequencies, int Length, Map<Character, String> Codes){
        double H = 0.0, H2 = 0.0;
        for(Map.Entry<Character, Integer> entry : frequencies.entrySet()){
            double P = entry.getValue().doubleValue()/(double)Length;
            H -= P*(Math.log(P)/Math.log(2));
            H2 += P*Codes.getOrDefault(entry.getKey(), null).length();
        }
        return H/H2;
    }
    
    //Hàm tạo cây Huffman
    private static Node buildHuffmanTree(Map<Character, Integer> frequencies) {
        PriorityQueue<Node> queue = new PriorityQueue<>(new HSCompare());
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            Node node = new Node(entry.getValue(), entry.getKey());
            queue.offer(node);
        }
        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            Node parent = new Node(left.frequency + right.frequency, '\0');
            parent.left = left;
            parent.right = right;
            queue.offer(parent);
        }
        return queue.poll();
    }
    
    //Hàm tạo cây Shanon
    private static Node buildShanonTree(Map<Character, Integer> frequencies) {
        PriorityQueue<Node> queue = new PriorityQueue<>(new HSCompare());
        int TotalFrequencies = 0;
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            TotalFrequencies += entry.getValue(); 
            Node node = new Node(entry.getValue(), entry.getKey());
            queue.offer(node);
        }

        if(queue.size() == 1){
            return queue.poll();
        }
        else{
            int Ratio = TotalFrequencies, BreakPoint = 0;
            //tìm điểm chia các ký tự thành 2 phần sao cho xác suất xuất hiện tương đương nhau
            for(int i=1; i<queue.size(); i++){
                PriorityQueue<Node> queue2 = new PriorityQueue<>(new HSCompare());
                queue2.addAll(queue);
                int Sum1 = 0, Sum2 = 0;
                
                int j = 1;
                do{
                    Node a1 = queue2.poll();
                    Sum1 += a1.frequency; 
                    j++;
                }
                while(j<=i);
                while(queue2.size() != 0){
                    Node a2 = queue2.poll();
                    Sum2 += a2.frequency;
                }

                if(Sum1 == Sum2){
                    Ratio = 0;
                    BreakPoint = i;
                    break;
                }else{
                    int Difference = (Sum1 > Sum2)?(Sum1-Sum2):(Sum2-Sum1);
                    if(Ratio > Difference){
                        BreakPoint = i;
                        Ratio = Difference;
                    }
                }
            }

            Node parent = new Node(TotalFrequencies, '\0');
            Map<Character, Integer> frequencies1 = new HashMap<>();
            for(int i = 0; i < BreakPoint; i++){
                Node a = queue.poll();
                frequencies1.put(a.data, a.frequency);
            }
            Map<Character, Integer> frequencies2 = new HashMap<>();
            while(queue.size() != 0){
                Node a = queue.poll();
                frequencies2.put(a.data, a.frequency);
            }
            parent.left = buildShanonTree(frequencies1);
            parent.right = buildShanonTree(frequencies2);
            return parent;
        }
    }
    
    //Hàm tạo code cho các ký tự
    private static Map<Character, String> genCharCodes(Node root) {
        Map<Character, String> codes = new HashMap<>();
        genCodesHelper(root, "", codes);
        for(Map.Entry<Character, String> entry : codes.entrySet()){
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        return codes;
    }
    
    //Hàm hỗ trợ đánh số cho cây
    private static void genCodesHelper(Node node, String code, Map<Character, String> codes) {
        if (node == null) {return;}
        if (node.left == null && node.right == null) {
            codes.put(node.data, code);
        }
        genCodesHelper(node.left, code + "0", codes);
        genCodesHelper(node.right, code + "1", codes);
    }
    
    //Hàm ghép các code của các ký tự để tạo thành một sâu mã Huffman hoàn chỉnh
    private static String encrypt(String input, Map<Character, String> codes) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : input.toCharArray()) {
            encrypted.append(codes.get(c));
        }
        return encrypted.toString();
    }
}
