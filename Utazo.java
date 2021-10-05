// az utaz´o¨ugyn¨ok probl´em´aj´anak aszimetrikus v´altozata - a
// v´arosok k¨oz¨otti t´avols´agok nem szimmetrikusak, azaz a ´es b k¨oz¨ott
// nem ugyanannyi, mint b ´es a k¨oz¨ott.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Utazo {
    public static double max(Ut[] p, int n, double kisebb) {
        double max = -10000000;
        for (int i = 0; i < n; i++) {
            if (p[i].getTavolsag() > max && p[i].getTavolsag() < kisebb) {
                max = p[i].getTavolsag();
            }
        }
        return max;
    }

    public static double minoseg(Integer[] S, int n, int[][] graf) {
        double MinosegS = 0;
        for (int l = 1; l < n; l++) {
            MinosegS += graf[S[l - 1]][S[l]];
        }
        MinosegS += graf[S[n - 1]][S[0]];
        return MinosegS;
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

        int PopulacioMeret = 10;
        double e = 0.05;       //parologtatasi egyutthato
        double[][] p = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                p[i][j] = 10000;
            }
            p[i][i] = 0;
        }

        double Legjobb = 0;
        Integer[] S = new Integer[n];
        boolean[] megNincs = new boolean[n];
        Ut[] C = new Ut[n];
        int szamlalo = 0;

        while (szamlalo < 20) {
            szamlalo++;
            Collection<Integer[]> P = new ArrayList<>();

            for (int j = 0; j < PopulacioMeret; j++) {
                for (int l = 0; l < n; l++) {
                    S[l] = 0;
                }
                for (int l = 0; l < n; l++) {
                    megNincs[l] = true;
                }
                int kezd = rand.nextInt(n);
                S[0] = kezd;
                megNincs[kezd] = false;

                for (int k = 1; k < n; k++) {
                    int index = 0;
                    Ut[] ut = new Ut[n - k + 1];
                    for (int l = 0; l < n; l++) {
                        if (megNincs[l]) {
                            ut[index] = new Ut(S[k - 1], l, p[S[k - 1]][l] / graf[S[k - 1]][l]);
                            index++;
                        }
                    }
                    double maxP = max(ut, index, Double.POSITIVE_INFINITY);
                    int m = index;
                    index = 0;
                    while (index == 0) {
                        for (int l = 0; l < m; l++) {
                            if (ut[l].getTavolsag() == maxP && megNincs[ut[l].getVaros2()]) {
                                C[index] = ut[l];
                                index++;
                            }
                        }
                        maxP = max(ut, m, maxP);
                    }

                    int r = rand.nextInt(index);
                    S[k] = C[r].getVaros2();
                    megNincs[S[k]] = false;
                }

                double MinosegS = minoseg(S, n, graf);
                if (Legjobb == 0 || MinosegS < Legjobb) {
                    Legjobb = MinosegS;
                }

                P.add(S);
            }

            for (int j = 0; j < n; j++) {
                for (int l = 0; l < n; l++) {
                    p[j][l] *= (1 - e);
                }
            }

            Iterator<Integer[]> iterator = P.iterator();
            while (iterator.hasNext()) {
                Integer[] P_ = iterator.next();

                for (int j = 1; j < n; j++) {
                    p[P_[j - 1]][P_[j]] += 1 / minoseg(P_, n, graf);
                }
            }
        }

        System.out.println(Legjobb);
    }
}
