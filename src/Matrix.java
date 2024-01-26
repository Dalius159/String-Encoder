import java.util.Scanner;

public class Matrix{ 
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws Exception{
        System.out.print("Nhap so hang va cot cua ma tran\nSo hang: ");
        int R = Integer.parseInt(sc.nextLine());
        System.out.print("So cot: ");
        int C = Integer.parseInt(sc.nextLine());

        double[] Pos = new double[R*C];
        System.out.println("Nhap xac xuat cua ma tran(nhap theo hang)...");
        for(int i=0; i<R*C; i++){            
            if(i%C == 0){
                System.out.println("Nhap xac suat hang " + (i/C + 1) + " cua ma tran:");
            }
            while(true){           
                String input = sc.nextLine();                  
                String[] parts = input.split("/");                 
                Pos[i] = (double)Integer.parseInt(parts[0])/(double)Integer.parseInt(parts[1]);
                if(Pos[i] < 0 || Pos[i] >1){
                    System.out.println("Xac suat phai nam trong khoang [0,1]. Yeu cau nhap lai!:");
                    continue;
                }
                else break;
            }
        }
        double[] Py = new double[C];
        for(int i = 0; i<C; i++){
            for(int j = 0; j<R; j++){
                Py[i]+=Pos[i + C*j];
            }
        }
        double[] Px = new double[R];
        for(int i = 0; i<R; i++){
            for(int j=0; j<C; j++){
                Px[i] +=Pos[C*i + j]; 
            }
        }
        double Hx = H(Px), Hy = H(Py), Hxy = Hxy(Pos);
        System.out.println("H(X) = " + Hx + "\nH(Y) = " + Hy + "\nH(X,Y) = " + Hxy);
        System.out.println("H(X|Y) = " + (Hxy - Hy) + "\nH(Y|X) = " + (Hxy - Hx));
        System.out.println("H(Y) - H(Y|X) = I(X;Y) =  " + (Hy + Hx - Hxy));
        System.out.println("D(X||Y) = " + D(Px,Py) + "\nD(Y||X) = " + D(Py,Px));
    }
    
    //Hàm tính H(Y) hoặc H(X)
    public static double H(double[] P){
        double H = 0;
        for(int i = 0; i<P.length; i++){
            if(P[i]!=0){
                H-=P[i]*(Math.log(P[i])/Math.log(2));
            }
        }
        return H;
    }

    //Hàm tính H(X,Y)
    public static double Hxy(double[] P){
        double Hxy = 0;
        for(int i = 0; i < P.length; i++){
            if(P[i]!=0){
                Hxy-=P[i]*(Math.log(P[i])/Math.log(2));
            }
        }
        return Hxy;
    }

    //Hàm tính D(X||Y) hoặc D(Y||X)
    public static double D(double[] P1, double[] P2){
        double D12 = 0;
        for(int i=0; i<P1.length; i++){
            D12+=(Double)P1[i]*(Math.log(P1[i]/P2[i])/Math.log(2));
        }
        return D12;
    }
}
