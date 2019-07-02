package Jogo;

/**
 * Created by up201405219 on 3/8/16.
 */
        import com.googlecode.lanterna.input.Key;
        import com.googlecode.lanterna.terminal.Terminal;

        import java.io.*;
        import java.util.Random;
        import java.util.Scanner;

        import static com.googlecode.lanterna.TerminalFacade.createSwingTerminal;
        import static java.lang.Thread.sleep;

public class Final {
    /////////////////////////////////////////////////////////////////
    //                      Tamanho da Matriz
    /////////////////////////////////////////////////////////////////
    public static int MAX = 4;  // Tamanho da matriz
    public static int[][] matriz = new int[MAX][MAX];

    public static int met = 1;  // Se for 0 executa pela shell, senão executa na janela

    public static boolean flag = false;  // true = pode por numeros random
    public static boolean flag_over = false;

    public static int pontos = 0;
    public static int best = 0;
    public static boolean finish_bool=finish();
    public static boolean over_bool=over();
    /////////////////////////////////////////////////////////////////
    //                      Janela
    /////////////////////////////////////////////////////////////////
    private Terminal term;
    private Terminal teste;
    public Final() throws InterruptedException {

        term = createSwingTerminal(46, 30);
        term.enterPrivateMode();
        term.setCursorVisible(false);

        while (!finish() && over()) {
            tofile();

            Key k = term.readInput();
            if (k != null) {
                System.out.println(k.getKind());
                switch (k.getKind()) {
                    case Escape:
                        term.exitPrivateMode();
                        term.clearScreen();
                        System.exit(0);
                        return;
                    case ArrowLeft:
                        go_put('a');
                        break;
                    case ArrowRight:
                        go_put('d');
                        break;
                    case ArrowDown:
                        go_put('s');
                        break;
                    case ArrowUp:
                        go_put('w');
                        break;
                    case Enter:
                        teste = createSwingTerminal(30, 10);
                        teste.enterPrivateMode();
                        teste.setCursorVisible(false);
                        show_window("Queres reiniciar o jogo?", 2, 2);
                        show_window("<- (SIM)", 4, 4);
                        show_window("(NÃO) ->", 17, 4);
                        boolean bol = true;
                        while (bol) {
                            Key y = teste.readInput();
                            if (y != null) {
                                switch (y.getKind()) {
                                    case ArrowRight:
                                        teste.exitPrivateMode();
                                        bol=false;
                                        break;
                                    case Escape:
                                        teste.exitPrivateMode();
                                        bol=false;
                                        break;
                                    case ArrowLeft:
                                        reset();
                                        teste.exitPrivateMode();
                                        bol=false;
                                        break;
                                }
                            }
                        }
                }
                term.clearScreen();
            }
            term.applySGR(Terminal.SGR.ENTER_BOLD);
            term.applyForegroundColor(Terminal.Color.WHITE);
            String string = null;
            for(int i=0; i<MAX; i++)
                for(int j=0; j<MAX; j++){
                    if(matriz[i][j]==0) {
                        term.applyForegroundColor(Terminal.Color.BLACK);
                        string = "";
                    }
                    else {
                        if (matriz[i][j] == 2)
                            term.applyForegroundColor(255,255,255);
                        else if (matriz[i][j] == 4)
                            term.applyForegroundColor(210,180,140);
                        else if (matriz[i][j] == 8)
                            term.applyForegroundColor(250,128,114);
                        else if (matriz[i][j] == 16)
                            term.applyForegroundColor(255,99,71);
                        else if (matriz[i][j] == 32)
                            term.applyForegroundColor(220,20,60);
                        else if (matriz[i][j] == 64)
                            term.applyForegroundColor(255,0,0);
                        else if (matriz[i][j] == 128)
                            term.applyForegroundColor(255,200,0);
                        else if (matriz[i][j] == 256)
                            term.applyForegroundColor(255,255,0);
                        else if (matriz[i][j] == 512)
                            term.applyForegroundColor(30,144,255);
                        else if (matriz[i][j] == 1024)
                            term.applyForegroundColor(0,0,139);
                        else if (matriz[i][j] == 2048)
                            term.applyForegroundColor(Terminal.Color.MAGENTA);
                        string = "" + matriz[i][j];
                    }
                    show(string, 13+(j*5), 12+(i*4));
                    show("****", 13+(j*5), 12+(i*4)+1);
                    show("****", 13+(j*5), 12+(i*4)-1);
                }
            tofile();
            term.applyForegroundColor(0,0,0);
            String pt = "Score: " + Integer.toString(pontos);

            show(pt, 13, 8);
            pt= "Best: " + best;
            show(pt, 13, 6);
            term.flush();
            try {
                sleep(10);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        String ttring="";
        if(finish()) {
            ttring = "YOU WIN!";
            term.applyForegroundColor(Terminal.Color.GREEN);
        }
        if(!over()) {
            ttring = "YOU LOSE!";
            term.applyForegroundColor(Terminal.Color.RED);
        }
        show(ttring, 18, 4);
        Thread.sleep(3000);
    }

    /////////////////////////////////////////////////////////////////
    //             Visualizar caracteres na janela
    /////////////////////////////////////////////////////////////////
    private void show(String str, int x, int y) {
        term.moveCursor(x, y);
        int len = str.length();

        for (int i = 0; i < len; i++)
            term.putCharacter(str.charAt(i));
    }

    private void show_window(String str, int x, int y) {
        teste.moveCursor(x, y);
        int len = str.length();

        for (int i = 0; i < len; i++)
            teste.putCharacter(str.charAt(i));
    }
    /////////////////////////////////////////////////////////////////
    //               Guardar o best num ficheiro
    /////////////////////////////////////////////////////////////////
    public static void tofile (){
        try{
            int scan;
            File file = new File("bestteste.txt");
            if(file.length()!=0) {
                Scanner ler = new Scanner(file);
                scan = ler.nextInt();
            }
            else
                scan=0;
            best=scan;
            if(pontos>=scan) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(Integer.toString(pontos));
                bw.flush();

                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                br.close();
                bw.close();
                best=pontos;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    /////////////////////////////////////////////////////////////////
    //                      Começar de novo
    /////////////////////////////////////////////////////////////////
    public static void reset(){
        for (int i = 0; i < MAX; i++)
            for (int j = 0; j < MAX; j++)
                matriz[i][j] = 0;
        put_rand();
        put_rand();
        pontos=0;
    }
    /////////////////////////////////////////////////////////////////
    //               "Main" para chamar a função
    /////////////////////////////////////////////////////////////////
    public static void go_put(char k){
        flag_over=false;
        go_play(k);

        if (flag)
            put_rand();

        flag = false;
        finish_bool=finish();
        over_bool=over();
    }
    /////////////////////////////////////////////////////////////////
    //               Função para testar as cores
    /////////////////////////////////////////////////////////////////
    public static void testarcores(){
        matriz[0][0]=2;
        matriz[0][1]=4;
        matriz[0][2]=8;
        matriz[0][3]=16;

        matriz[1][0]=32;
        matriz[1][1]=64;
        matriz[1][2]=128;
        matriz[1][3]=256;

        matriz[2][0]=512;
        matriz[2][1]=1024;
        matriz[2][2]=2048;
        matriz[2][3]=2;
    }
    /////////////////////////////////////////////////////////////////
    //                     Imprimir a matriz
    /////////////////////////////////////////////////////////////////
    public static void go_print() {
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                if(matriz[i][j] == 0)
                    System.out.print("_" + "\t");
                else
                    System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Score: " + pontos);
        System.out.println("Best: " + best);
    }
    /////////////////////////////////////////////////////////////////
    //                     Meter numero randon
    /////////////////////////////////////////////////////////////////
    public static void put_rand() {
        Random nrand = new Random();
        int tam=0;
        int mat_cl[][] = new int[MAX*MAX][2];
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                if (matriz[i][j] == 0) {
                    mat_cl[tam][0] = i;
                    mat_cl[tam][1] = j;
                    tam++;
                }
            }
        }
        int c = nrand.nextInt(tam);
        int q = nrand.nextInt(2);
        if (q == 0) q = 2;
        else q = 4;
        matriz[mat_cl[c][0]][mat_cl[c][1]]=q;
    }
    /////////////////////////////////////////////////////////////////
    //                 Verificar se já fez o 2048
    /////////////////////////////////////////////////////////////////
    public static boolean finish() {
        for (int i = 0; i < MAX; i++)
            for (int j = 0; j < MAX; j++)
                if (matriz[i][j] == 2048)
                    return true;
        return false;
    }
    /////////////////////////////////////////////////////////////////
    //                     Clicar numa tecla
    /////////////////////////////////////////////////////////////////
    public static void go_play(char dir) {
        int[] vet = new int[MAX];
        if (dir == 'a' || dir == 'A') {
            for (int i = 0; i < MAX; i++) {
                for (int j = 0; j < MAX; j++) {
                    vet[j] = matriz[i][j];
                }
                play(vet);
                for(int k=0; k<MAX; k++)
                    matriz[i][k]=vet[k];
            }
        }
        else if (dir == 'w' || dir == 'W') {
            for (int j = 0; j < MAX; j++) {
                for (int i = 0; i < MAX; i++) {
                    vet[i] = matriz[i][j];
                }
                play(vet);
                for(int k=0; k<MAX; k++)
                    matriz[k][j]=vet[k];
            }
        }
        else if (dir == 'd' || dir == 'D') {
            for (int i = 0; i < MAX; i++) {
                for (int j = 0; j < MAX; j++) {
                    vet[j] = matriz[i][MAX-1-j];
                }
                play(vet);
                for(int k=0; k<MAX; k++)
                    matriz[i][MAX-1-k]=vet[k];
            }
        }
        else if (dir == 's' || dir == 'S') {
            for (int j = 0; j < MAX; j++) {
                for (int i = 0; i < MAX; i++) {
                    vet[i] = matriz[MAX-1 - i][j];
                }
                play(vet);
                for (int k = 0; k < MAX; k++)
                    matriz[MAX-1 - k][j] = vet[k];
            }
        }
    }
    /////////////////////////////////////////////////////////////////
    //                   Verificar e fazer somas
    /////////////////////////////////////////////////////////////////
    public static void play(int v[]){
        del_zero(v);
        for(int i = 0; i<MAX-1; i++)
            if(v[i]==v[i+1] && v[i]!=0) {
                v[i] += v[i + 1];
                v[i + 1] = 0;
                pontos += v[i];
                flag = true;
                del_zero(v);
            }
        del_zero(v);
    }
    /////////////////////////////////////////////////////////////////
    //          "Puxar" o conteúdo do vetor, eleminar os 0
    /////////////////////////////////////////////////////////////////
    public static void del_zero(int v[]){
        int temp, k=MAX;
        while(k>0) {
            for (int i = 0; i < MAX; i++)
                if (v[i] == 0)
                    for (int j = i; j < MAX-1; j++) {
                        if(v[j+1]!=0)
                            flag=true;
                        temp = v[j];
                        v[j] = v[j + 1];
                        v[j + 1] = temp;
                    }
            k--;
        }
    }
    /////////////////////////////////////////////////////////////////
    //                    Verificar se perdeu
    //                  Não tem mais movimentos
    /////////////////////////////////////////////////////////////////
    //                Todas as direções possiveis
    /////////////////////////////////////////////////////////////////
    public static boolean over(){
        int[] v = new int[MAX];

        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                v[j] = matriz[i][j];
            }
            play_over(v);
        }
        for (int j = 0; j < MAX; j++) {
            for (int i = 0; i < MAX; i++){
                v[i] = matriz[i][j];
            }
            play_over(v);
        }
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                v[j] = matriz[i][MAX-1-j];
            }
            play_over(v);
        }
        for (int j = 0; j < MAX; j++) {
            for (int i = 0; i < MAX; i++) {
                v[i] = matriz[MAX-1 - i][j];
            }
            play_over(v);
        }
        if(flag_over)
            return true;
        else
            return false;
    }
    /////////////////////////////////////////////////////////////////
    //           Verificar se é possivél fazer somas
    /////////////////////////////////////////////////////////////////
    public static void play_over(int v[]){
        del_zero_over(v);
        for(int i = 0; i<MAX-1; i++)
            if(v[i]==v[i+1] && v[i]!=0) {
                v[i] += v[i + 1];
                v[i + 1] = 0;
                flag_over = true;
                del_zero_over(v);
            }
        del_zero_over(v);
    }
    /////////////////////////////////////////////////////////////////
    //          "Puxar" o conteúdo do vetor, eleminar os 0
    /////////////////////////////////////////////////////////////////
    public static void del_zero_over(int v[]){
        int temp, k=MAX;
        while(k>0) {
            for (int i = 0; i < MAX; i++)
                if (v[i] == 0)
                    for (int j = i; j < MAX-1; j++) {
                        if(v[j+1]!=0)
                            flag_over=true;
                        temp = v[j];
                        v[j] = v[j + 1];
                        v[j + 1] = temp;
                    }
            k--;
        }
    }
    /////////////////////////////////////////////////////////////////
    //                          Main
    /////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws InterruptedException {

        put_rand();
        put_rand();
        tofile();
        //testarcores();
        if(met==1)
            new Final();
        else {
            Scanner ler = new Scanner(System.in);
            go_print();
            boolean finish = finish();
            boolean over = over();
            char k;
            while (!finish && over) {
                flag_over = false;
                k = ler.next().charAt(0);
                go_play(k);

                if (flag)
                    put_rand();

                flag = false;

                go_print();
                finish = finish();
                over = over();
                System.out.println(finish + " " + over);
            }
            if (finish)
                System.out.println("YOU WIN!");
            else
                System.out.println("YOU LOSE!");
        }
        System.exit(0);
    }
}