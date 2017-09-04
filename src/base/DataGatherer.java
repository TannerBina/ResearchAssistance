package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Makes internet queues to obtain data.
 * Created by Tanner on 1/23/2017.
 */
public class DataGatherer implements Runnable{

    private Thread runner;
    private int startIndex;
    private int numResults;
    private String name;
    private String search;

    public DataGatherer(int startIndex, int numResults, String search) {
        this.search = search;
        this.startIndex = startIndex;
        this.numResults = numResults;
        name = "DataGatherer" + startIndex +"-" + numResults;
        runner = new Thread(this, name);
        runner.start();
    }

    private enum INFOTAG {
            __RATING__, __CITATIONS__, __KEYWORDS__, __IDENTIFIER__, __URL__, __TITLE__,
        __PUBLICATIONNAME__, __PUBLISHER__, __PUBDATE__, __GENRE__, __ABSTRACT__, __AUTHORS__
    };

    public void run(){
        try{
            String start = new String();
            start += startIndex;
            String num = new String();
            num += numResults;

            ProcessBuilder builder = new ProcessBuilder("python", "puller.py", start, num, search);
            Process sProc = builder.start();
            BufferedReader output = new BufferedReader
                    (new InputStreamReader(sProc.getInputStream()));

            String s = null;

            StringBuilder resultBuilder = new StringBuilder();

            while((s = output.readLine()) != null){
                resultBuilder.append(s);
                resultBuilder.append('\n');
            }


            while(resultBuilder.toString().trim().length() != 0){
                int startIndex = resultBuilder.indexOf("__START__");
                int endIndex = resultBuilder.indexOf("__END__");
                String current = resultBuilder.substring(startIndex + "__START__".length(), endIndex);

                resultBuilder.delete(startIndex, endIndex + "__END__".length());

                Scanner scan = new Scanner(current);
                String[] a = new String[INFOTAG.values().length];
                while(scan.hasNextLine()){
                    String next = scan.nextLine();
                    startIndex = next.indexOf("__b'");
                    endIndex = next.indexOf("'__");
                    System.out.println(next);

                    if (startIndex != -1 && endIndex != -1){
                        String tag = next.substring(0, startIndex + "__".length());
                        String res = next.substring(startIndex + "__b'".length(), endIndex);

                        a[INFOTAG.valueOf(tag).ordinal()] = res;
                    }
                }

                System.out.println(a[10]);
                System.out.println(a[11]);
                System.out.println();

                Controller.addDoc(new Document(a[0], a[1], a[2], a[3],
                        a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11]));
            }

/*
            while((s = output.readLine()) != null){
                String[] a = new String[Constants.DOCFIELDS];

                a[0] = s;
                for (int i = 1; i < Constants.DOCFIELDS; i++){
                    s = output.readLine();
                    System.out.println(s);

                    if (i > 2 && s != null){
                        s = s.substring(2, s.length()-1);
                    }

                    a[i] = s;
                }
                Controller.addDoc(new Document(a[0], a[1], a[2], a[3],
                        a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11]));
            }*/

            output.close();
        } catch (IOException e){
            System.err.println("Error in DataPuller.py");
        }
    }
}
