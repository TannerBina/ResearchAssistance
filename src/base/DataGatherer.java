package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

            while((s = output.readLine()) != null){
                String[] a = new String[Constants.DOCFIELDS];

                a[0] = s;
                for (int i = 1; i < Constants.DOCFIELDS; i++){
                    s = output.readLine();

                    if (i > 2 && s != null){
                        s = s.substring(2, s.length()-1);
                    }

                    a[i] = s;
                }
                Controller.addDoc(new Document(a[0], a[1], a[2], a[3],
                        a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11]));
            }

            output.close();
        } catch (IOException e){
            System.err.println("Error in DataPuller.py");
        }
    }
}
