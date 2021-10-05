import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class SimaUtazo {
    public static Integer legkozelebbi(int i, int n, boolean[] megNincs, int[][] graf) {
        int min = 10000000;
        Integer minInd = 0;
        for (int j = 0; j < n; j++) {
            if (megNincs[j] && graf[i][j] < min) {
                min = graf[i][j];
                minInd = j;
            }
        }
        return minInd;
    }

    public static int korHossza(Integer k, int n, boolean[] megNincs, int[][] graf, Integer[] kor) {
        for (int i = 0; i < n; i++) {
            megNincs[i] = true;
        }
        kor[0] = k;
        megNincs[k] = false;
        int tav = 0;
        for (int i = 1; i < n; i++) {
            kor[i] = legkozelebbi(kor[i - 1], n, megNincs, graf);
            megNincs[kor[i]] = false;
            tav += graf[kor[i - 1]][kor[i]];
        }
        return tav + graf[kor[n - 1]][k];
    }

    public static int legRovidebbKor(int n, boolean[] megNincs, int[][] graf, Integer[] minKor) {
        int minTav = 100000;
        Integer[] kor = new Integer[n];
        for (int k = 0; k < n; k++) {
            int tav = korHossza(k, n, megNincs, graf, kor);
            if (tav < minTav) {
                minTav = tav;
                for (int i = 0; i < n; i++) {
                    minKor[i] = kor[i];
                }
            }
        }
        return minTav;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Random rand = new Random();
        File read = new File("Teszt3.txt");
        Scanner reader = new Scanner(read);

        int n = reader.nextInt();
        int[][] graf = new int[n][n];
        for (int i = 0; i < n; i++) {
            graf[i][i] = 0;
        }
        while (reader.hasNext()) {
            int varos1, varos2, tav;
            varos1 = reader.nextInt();
            varos2 = reader.nextInt();
            tav = reader.nextInt();
            graf[varos1][varos2] = tav;
        }

        boolean[] megNincs = new boolean[n];
        Integer[] minKor = new Integer[n];

        System.out.println(legRovidebbKor(n, megNincs, graf, minKor));
    }
}
