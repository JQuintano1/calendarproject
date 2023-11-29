import java.time.ZonedDateTime;

public class planning {
    private ZonedDateTime date;
    private String plan;

    public planning(ZonedDateTime date, String plan){
        this.date = date;
        this.plan = plan;
    }

    public ZonedDateTime getDate() {
        return date;
    }
    
    public void setDate(ZonedDateTime date){
        this.date = date;
    }

    public String getplan(){
        return plan;
    }

    public void setplan(String plan){
        this.plan = plan;
    }

    


    @Override
    public String toString() {
        return  "planning{" + "date=" + date + ", plan='" + plan + '\'' + '}';
    }
}




