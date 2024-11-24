package application;
	
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;
import java.util.Calendar;
import java.util.LinkedList;
import java.io.IOException;
import javafx.scene.control.ComboBox;
import java.util.NoSuchElementException;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.time.Month;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import javafx.scene.layout.Region;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {		
			CalendarPage page = new CalendarPage(LocalDate.now());
			
			// Setting up panes
			BorderPane root = new BorderPane();
			GridPane grid = page.setCalendar();
						
			HBox hbox = page.setTitle();
			
			
			// Set up the title (hbox) and calendar (grid) on screen
			root.setTop(hbox);
			root.setCenter(grid);
			BorderPane.setAlignment(grid, Pos.CENTER);
						
			
			// Create the scene
			Scene scene = new Scene(root,800,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			// setting the scene and showing it
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
