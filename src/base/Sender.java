package base;

/**
 * Creates multiple threads
 * Created by Tanner on 1/24/2017.
 */
public class Sender implements Runnable{

    //threat that sends out other threads
    private Thread runner;
    //number of short, mid and long term threads
    private final int SHORTTERM = 50;
    private final int MIDTERM = 10;
    private final int LONGTERM = 5;
    private String input;

    /*
    Create a sender with a given input string
     */
    public Sender(String input){
        this.input = input;
        runner = new Thread(this, "Gatherer Sender");
        runner.start();
    }

    /*
    Send out all data gatherers
     */
    public void run(){
        for (int i = 1; i <= SHORTTERM; i++){
            DataGatherer gatherer = new DataGatherer(i, 1, input);
        }
        for (int i = 0; i < MIDTERM; i++){
            DataGatherer gatherer = new DataGatherer(51+i*10, 10, input);
        }
        for (int i = 0; i < LONGTERM; i++){
            DataGatherer gatherer = new DataGatherer(151+i*100, 100, input);
        }
    }
}
