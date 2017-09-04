package base;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class Controller {

    //text field for search input box
    @FXML
    private TextField searchInput;
    //holds the current article data
    @FXML
    private TextArea articleBox;
    //states the page on and the total
    @FXML
    private Label pageLabel;
    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;

    //vector of all documents, synced with all threads
    private static Vector<Document> docs;

    //the current document the user is on
    private static int currentDocNum;

    /*
    Base constructor
     */
    public Controller(){
    }

    /*
    Initializes the gui to base values.
     */
    @FXML
    private void initialize(){
        //makes text wrap
        articleBox.setWrapText(true);

        //set init values
        currentDocNum = 0;
        pageLabel.setText("Article 0 of 0");
        docs = new Vector<>();
    }

    /*
    searches for the search query using threads.
    Adds to doc list and displays next doc
     */
    @FXML
    private void search(){
        //reset values and get search
        currentDocNum = 1;
        String input = searchInput.getText();
        docs = new Vector<>();

        Sender sender = new Sender(input);

        //wait until at least two documents have been found
        while(docs.size() < 2){
        }

        //update page.
        updatePageLabel();
        docs.get(0).displayDoc(articleBox);
    }

    /*
    Adds a document to the list of current documents
     */
    public static void addDoc(Document toAdd){
        //if the document is blank, do nothing
        if (! toAdd.isInitialized()) return;

        //attempt to add in correct place
        boolean added = false;
        //look through all further documents for where to put it, if its rating is high enough
        for (int i = currentDocNum+1; i < docs.size(); i++){
            if (docs.get(i).getRating() < toAdd.getRating()){
                try {
                    docs.insertElementAt(toAdd, i - 1);
                } catch (Exception e){
                    System.out.println("Attempted add at: " + (i-1));
                    System.out.println("Size: " + docs.size());
                }
                added = true;
                break;
            }
        }
        //if it has not been added for some reason, add to end.
        if (!added){
            docs.add(toAdd);
        }
    }

    /*
    Updates the page label
     */
    private void updatePageLabel(){
        pageLabel.setText("Article " + currentDocNum + " of " + docs.size());
    }

    /*
    Loads the next document in the list of docs
     */
    @FXML
    private void loadNextDoc(){
        if (currentDocNum < docs.size()){
            currentDocNum++;
            updatePageLabel();
            docs.get(currentDocNum-1).displayDoc(articleBox);
        }
    }

    /*
    Load the previous document in the list
     */
    @FXML
    private void loadPrevDoc(){
        if (currentDocNum > 1){
            currentDocNum--;
            updatePageLabel();
            docs.get(currentDocNum-1).displayDoc(articleBox);
        }
    }

    /*
    Open the web page for the current doc
     */
    @FXML
    private void openDoc(){
        if (!docs.isEmpty()) {
            //open the url given with the doc
            if (Desktop.isDesktopSupported()){
                try {
                    Desktop.getDesktop().browse
                            (new URI(docs.get(currentDocNum-1).getUrl()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
