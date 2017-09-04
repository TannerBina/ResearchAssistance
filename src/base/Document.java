package base;

import javafx.scene.control.TextArea;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Stores info for a document
 * Created by Tanner on 1/17/2017.
 */
public class Document {
    private String rating;
    private String citations;
    private String keywords;
    private String ident;
    private String url;
    private String title;
    private String pubName;
    private String publisher;
    private String pubDate;
    private String genre;
    private String abst;
    private List<Author> authList;

    public Document(String rating, String citations, String keywords, String ident, String url, String title, String pubName,
                    String publisher, String pubDate, String genre, String abst,
                    String authors){
        this.rating = rating;
        this.citations = citations;
        this.keywords = keywords;
        this.ident = ident;
        this.url = url;
        this.title = title;
        this.pubName = pubName;
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.genre = genre;
        this.abst = abst;
        authList = new LinkedList<>();
        try {
            Scanner s = new Scanner(authors);
            StringBuilder sb = new StringBuilder();
            while (s.hasNext()) {
                String next = s.next();
                if (next.contains(":")) {
                    authList.add(new Author(sb.toString()));
                    sb = new StringBuilder();
                } else {
                    sb.append(next);
                    sb.append(" ");
                }
            }
        } catch (Exception e){

        }
    }

    public double getRating(){
        return Double.parseDouble(rating);
    }

    public String getUrl() {
        return url;
    }

    public boolean isInitialized(){
        return (rating != null && ident != null && title != null && pubDate != null);
    }

    public String getCitation(){
        StringBuilder sb = new StringBuilder();
        if (authList.size() != 0){
            for (int i = 0; i < authList.size(); i++){
                sb.append(authList.get(i).getName());
                if (i != authList.size()-1){
                    sb.append("& ");
                }
            }
        }
        sb.append("(");
        sb.append(pubDate);
        sb.append("). ");
        sb.append(title);
        sb.append(". ");
        sb.append(pubName);
        sb.append(". ");
        sb.append(ident);
        return sb.toString();
    }

    public void displayDoc(TextArea area){
        StringBuilder string = new StringBuilder();
        string.append("Research Assistant Rating: ");
        string.append(rating);
        string.append('\n');
        string.append('\n');

        string.append("Title: ");
        string.append(title);
        string.append('\n');
        string.append('\n');

        string.append("Genre: ");
        string.append(genre);
        string.append('\n');
        string.append('\n');

        string.append("Keywords: ");
        string.append(keywords);
        string.append('\n');
        string.append('\n');

        string.append("Authors");
        for (Author auth: authList){
            string.append(": ");
            string.append(auth.getName());
        }
        string.append('\n');
        string.append('\n');

        string.append("Publication Name: ");
        string.append(pubName);
        string.append('\n');
        string.append('\n');

        string.append("Publisher: ");
        string.append(publisher);
        string.append('\n');
        string.append('\n');

        string.append("Publication Date: ");
        string.append(pubDate);
        string.append('\n');
        string.append('\n');

        string.append("Identifier: ");
        string.append(ident);
        string.append('\n');
        string.append('\n');

        string.append("Abstract: ");
        string.append(abst);
        string.append('\n');
        string.append('\n');

        string.append("APA Citation: ");
        string.append(getCitation());

        area.setText(string.toString());
    }
}
