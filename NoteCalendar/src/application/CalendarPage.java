package application;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Set;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public class CalendarPage {
	LocalDate currDate;
	private Month month;
	private int numMonth;
	private int year;
	private int gridCount;
	private GridPane grid;
	private final String[] days = { "Mon", "Tues", "Wed", "Thu", "Fri", "Sat", "Sun" };
	private Region[] region = new Region[2];

	public CalendarPage(LocalDate currDate) {

		// record current month, year, and day
		this.currDate = currDate;
		month = currDate.getMonth();
		numMonth = month.getValue();
		year = currDate.getYear();
		grid = new GridPane();

	}
	
	private Region[] changeMonth(int direction) {
		
		month = (direction >= 0) ? month.plus(1) : month.minus(1);
		
		// checking if you went to next year
		if (month.getValue() == 1 && numMonth == 12) {
			year++;
		} 
		
		// setting to next month
		numMonth = month.getValue();
		
//		System.out.println(month.getValue());
		
		// setting up calendar again
		HBox title = setTitle(); 
		grid = setCalendar();
				
		region[0] = title;
		region[1] = grid; 
		

		
		return region;
	}

	public HBox setTitle() {
		HBox hbox = new HBox();
		
		// make buttons and title
		Button backButton = new Button("<");
		Button nextButton = new Button(">");
		Text currMonth = new Text(month.toString());
		
		StringProperty currText = new SimpleStringProperty("Bound Text");
		
		currMonth.textProperty().bind(currText);
		
		currText.set(month.toString());
		
		// setting button Ids
		backButton.setId("back");
		nextButton.setId("next");
						
		currMonth.setFont(Font.font("New York Times", FontWeight.BOLD, 25));
		
		backButton.setOnAction(x -> {
			changeMonth(-1);
		});
		
		nextButton.setOnAction(x -> {
			changeMonth(1);
		});
		
		// put the buttons and title in the hbox
		hbox.getChildren().add(backButton);
		hbox.getChildren().add(currMonth);
		hbox.getChildren().add(nextButton);
						
		// set margins and padding
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(3));
		HBox.setMargin(currMonth, new Insets(5));
		
		return hbox;
	}
	
	public GridPane setCalendar() {
		
		// sets the number of grids needed for the month
		setGridCount();

		// sets the headers for the calendar
		setHeader(grid);

		// check at what weekday the month starts in
		int firstDay = getFirstDay(month).getValue() - 1;

		// sets up the calendar to fill up the days of the previous month
		setUpPreviousMonth(grid, firstDay);

		int row = 0;
		int col = 0;

		// Place buttons in the grid, making sure they don't overlap
		for (int i = 0; i < gridCount; i++) {
			Button button = new Button();
			button.setText("" + (i + 1));
			button.setId(month.name() + (i+1));

			button.setMinSize(100, 100);
			button.setMaxSize(100, 100);

			// Calculate the row and column for each button
			row = (i + firstDay) / 7 + 1; // 7 buttons per row (for a 7-day week layout)
			col = (i + firstDay) % 7; // Place buttons in columns, with 7 per row

			// Add the button to the grid at the calculated position (row, col)
			grid.add(button, col, row);

		}

		if (col != 6 || row != 6) {
			// sets up days for the remaining week if calendar is not filled
			setUpRestOfDays(grid, row, col);
		}

		// setting the alignment
		grid.setAlignment(Pos.CENTER);
		
		return grid;
	}
	
	public Button findButton(BorderPane root, String id) {
		return ((Button) root.lookup("#"+id));
	}

	/**
	 * Checks if the current year is a leap year
	 * 
	 * @return true if it is leap year, false if it is not
	 */
	private boolean isLeapYear() {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, this.year);

		return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;

	}


	private void setGridCount() {

		Set<Integer> thirtyDayMonths = Set.of(4, 6, 9, 11);

		// checking if the month has 30 or 31 days, or if it is February
		if (numMonth == 2) {

			if (isLeapYear()) {
				gridCount = 29;
			} else {
				gridCount = 28;
			}

		} else if (thirtyDayMonths.contains(numMonth)) {
			gridCount = 30;
		} else {
			gridCount = 31;
		}
	}

	private void setUpPreviousMonth(GridPane grid, int firstDay) {
		// check the number of days in the previous month
		int lastDay = YearMonth.of(year, month.minus(1)).lengthOfMonth();

		// inputting the dates from the previous month to fill the calendar
		for (int k = firstDay - 1; k >= 0; k--) {

			Button button = new Button();
			button.setText("" + lastDay);

			// setting button size
			button.setMinSize(100, 100);
			button.setMaxSize(100, 100);

			int row = k / 7 + 1;
			int col = k % 7;

			grid.add(button, col, row);
			lastDay--;

		}
	}

	
	private void setUpRestOfDays(GridPane grid, int row, int col) {
		int currDay = 1;
		
		boolean setReq = (row != 6) ? (row != 6) : (col != 6);

		while (col != 6) {

			Button button = new Button();
			button.setText("" + currDay);

			// setting button size
			button.setMinSize(100, 100);
			button.setMaxSize(100, 100);

			currDay++;
			col++;

			grid.add(button, col, row);

		}
	}

	private void setHeader(GridPane grid) {
		// Making the weekday headers for the calendar
		for (int j = 0; j < 7; j++) {
			Text day = new Text(days[j]);
			day.setFont(Font.font("New York Times", FontWeight.BOLD, 15));

			int col = j % 7;

			grid.add(day, col, 0);

			GridPane.setMargin(day, new Insets(10));
			GridPane.setHalignment(day, HPos.CENTER);
			GridPane.setValignment(day, VPos.CENTER);
		}
	}

	private DayOfWeek getFirstDay(Month currMonth) {

		return LocalDate.of(year, currMonth, 1).getDayOfWeek();

	}

	public LocalDate getCurrentDate() {
		return currDate;
	}

	public Month getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

}
