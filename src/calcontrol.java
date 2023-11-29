import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

import javax.swing.Action;

public class calcontrol implements Initializable{

    ZonedDateTime date;
    ZonedDateTime today;

    @FXML
    private FlowPane calformat;

    @FXML
    private Text month;

    @FXML
    private Text year;

    @FXML
    private TextField hours;

    @FXML
    private TextField minutes;

    @FXML
    private TextField scheduleday;

    @FXML
    private TextField schedulemonth;
    
    @FXML
    private TextField scheduleyear;

    @FXML
    private TextField eventmessage;

    @FXML
    private Button setevent;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        date = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }
    @FXML
    void back(ActionEvent event) {
        date = date.minusMonths(1);
        calformat.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forward(ActionEvent event) {
        date = date.plusMonths(1);
        calformat.getChildren().clear();
        drawCalendar();
    }
    @FXML
    void schedevent(ActionEvent e) {
        if (e.getSource()==setevent)
        getCalendarActivitiesMonth(date);
        drawCalendar();
        
    }

    private void drawCalendar(){
        year.setText(String.valueOf(date.getYear()));
        month.setText(String.valueOf(date.getMonth()));

        double calendarWidth = calformat.getPrefWidth();
        double calendarHeight = calformat.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calformat.getHgap();
        double spacingV = calformat.getVgap();

        //List of activities for a given month
        Map<Integer, List<planning>> calendarActivityMap = getCalendarActivitiesMonth(date);

        int monthMaxDate = date.getMonth().maxLength();
        //Check for leap year
        if(date.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(date.getYear(), date.getMonthValue(), 1,0,0,0,0,date.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<planning> calendarActivities = calendarActivityMap.get(currentDate);
                        if(calendarActivities != null){
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if(today.getYear() == date.getYear() && today.getMonth() == date.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.GREEN);
                    }
                }
                calformat.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<planning> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On ... click print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getplan() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                System.out.println(text.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }


    private Map<Integer, List<planning>> createCalendarMap(List<planning> calendarActivities) {
        Map<Integer, List<planning>> calendarActivityMap = new HashMap<>();

        for (planning activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<planning> OldListByDate = calendarActivityMap.get(activityDate);

                List<planning> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return  calendarActivityMap;
    }

    private Map<Integer, List<planning>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<planning> calendarActivities = new ArrayList<>();
        int year = Integer.parseInt(scheduleyear.getText());
        int month = Integer.parseInt(schedulemonth.getText());
        int day = Integer.parseInt(scheduleday.getText());
        String reminder = eventmessage.getText();

        
        ZonedDateTime time = ZonedDateTime.of(year, month, day, 16, 30, 0, 0,dateFocus.getZone());
        calendarActivities.add(new planning(time, reminder));

        

        
        return createCalendarMap(calendarActivities);
        
    }
    
    

    

}
