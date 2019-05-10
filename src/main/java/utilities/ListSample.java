package utilities;

public class ListSample {
    public String id;
    public String name;
    public Boolean closed;
    public String idBoard;
    public int pos;
    public String subscribed;
    public String softLimit;


    public ListSample(){

    }

    public ListSample(String id,String name,Boolean closed,
                      String idBoard,int pos,
                      String subscribed,String softLimit){
        this.id=id;
        this.name=name;
        this.closed=closed;
        this.idBoard=idBoard;
        this.pos=pos;
        this.subscribed=subscribed;
        this.softLimit=softLimit;


    }

}
