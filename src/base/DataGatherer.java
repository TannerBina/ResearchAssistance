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

    //thread that runs py scripts
    private Thread runner;

    //start index of gatherer
    private int startIndex;
    //number of results to pull
    private int numResults;
    //name of gatherer
    private String name;
    //search result
    private String search;

    /*
    Iniatilize the data gatherer and start it
     */
    public DataGatherer(int startIndex, int numResults, String search) {
        this.search = search;
        this.startIndex = startIndex;
        this.numResults = numResults;
        name = "DataGatherer" + startIndex +"-" + numResults;
        runner = new Thread(this, name);
        runner.start();
    }

    /*
    All given info tags
     */
    private enum INFOTAG {
            __RATING__, __CITATIONS__, __KEYWORDS__, __IDENTIFIER__, __URL__, __TITLE__,
        __PUBLICATIONNAME__, __PUBLISHER__, __PUBDATE__, __GENRE__, __ABSTRACT__, __AUTHORS__
    };

    /*
    Send the scripts out
     */
    public void run(){
        try{
            String start = new String();
            start += startIndex;
            String num = new String();
            num += numResults;

            //starts the python script with given inptus
            ProcessBuilder builder = new ProcessBuilder("python", "puller.py", start, num, search);
            Process sProc = builder.start();
            //change input sctream to python output
            BufferedReader output = new BufferedReader
                    (new InputStreamReader(sProc.getInputStream()));

            String s = null;

            //build the results from input string
            StringBuilder resultBuilder = new StringBuilder();

            while((s = output.readLine()) != null){
                resultBuilder.append(s);
                resultBuilder.append('\n');
            }


            //parse results
            //more results while outut isnt empty
            while(resultBuilder.toString().trim().length() != 0){
                //get single doc block
                int startIndex = resultBuilder.indexOf("__START__");
                int endIndex = resultBuilder.indexOf("__END__");
                String current = resultBuilder.substring(startIndex + "__START__".length(), endIndex);

                //delete the doc taken
                resultBuilder.delete(startIndex, endIndex + "__END__".length());

                Scanner scan = new Scanner(current);
                String[] a = new String[INFOTAG.values().length];
                //scan and parse individual doc
                while(scan.hasNextLine()){
                    String next = scan.nextLine();
                    startIndex = next.indexOf("__b'");
                    endIndex = next.indexOf("'__");

                    if (startIndex != -1 && endIndex != -1){
                        //get tag and res
                        String tag = next.substring(0, startIndex + "__".length());
                        String res = next.substring(startIndex + "__b'".length(), endIndex);

                        //put the info into the array
                        a[INFOTAG.valueOf(tag).ordinal()] = res;
                    }
                }



                //add the doc
                Controller.addDoc(new Document(a[0], a[1], a[2], a[3],
                        a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11]));
            }

            output.close();
        } catch (IOException e){
            System.err.println("Error in DataPuller.py");
        }
    }
}
