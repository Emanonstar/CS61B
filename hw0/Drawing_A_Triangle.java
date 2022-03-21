public class Drawing_A_Triangle {
    public static void main(String[] args) {
        int n = 1;
        while(n <= 5){
            int i = 0;
            while(i < n){
                System.out.print('*');
                i++;
            }
            System.out.println();
            n++;
        }
    }
}