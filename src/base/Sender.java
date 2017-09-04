package base;

/**
 * Creates multiple threads
 * Created by Tanner on 1/24/2017.
 */
public class Sender implements Runnable{

    private Thread runner;
    private final int SHORTTERM = 100;
    private final int MIDTERM = 100;
    private final int LONGTERM = 10;
    private String input;

    public Sender(String input){
        this.input = input;
        runner = new Thread(this, "Gatherer Sender");
        runner.start();
    }

    public void run(){
        for (int i = 1; i <= SHORTTERM; i++){
            DataGatherer gatherer = new DataGatherer(i, 1, input);
        }
        for (int i = 0; i < MIDTERM; i++){
            DataGatherer gatherer = new DataGatherer(101+i*10, 10, input);
        }
        for (int i = 0; i < LONGTERM; i++){
            DataGatherer gatherer = new DataGatherer(1101+i*100, 100, input);
        }
    }
}
