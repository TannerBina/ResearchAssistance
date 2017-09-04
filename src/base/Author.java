package base;

/**
 * Holds an author name. Currently only holds name.
 * Will increase to hold more data about specific authors.
 * Such as, papers published, papers on subject published,
 * and times cited
 * Created by Tanner on 1/17/2017.
 */
public class Author {
    //holds name
    private String name;

    /*
    Basic constructor
     */
    public Author(String name){
        this.name = name;
    }

    /*
    Returns name of author
     */
    public String getName(){
        return name;
    }
}
