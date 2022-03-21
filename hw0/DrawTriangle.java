public class DrawTriangle {
    public static void drawTriangle(int N) {
        int n = 1;
        while(n <= N){
            int i = 0;
            while(i < n){
                System.out.print('*');
                i++;
            }
            System.out.println();
            n++;
        }
    }
    
    public static void main(String[] args) {
        drawTriangle(10);
    }
}