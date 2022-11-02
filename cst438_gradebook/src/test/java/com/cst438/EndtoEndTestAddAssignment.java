package com.cst438;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;



@SpringBootTest
public class EndtoEndTestAddAssignment{
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32(1)/chromedriver.exe";

	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment";
	public static final String TEST_COURSE_TITLE = "Test Course";
	public static final String TEST_STUDENT_NAME = "Test";
	public static final int TEST_COURSE_ID = 99999;
	public static final String TEST_DUE_DATE = "2022-11-01";
	
	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Test
	public void addAssignmentTest() throws Exception {
		
		Course c = new Course();
		c.setCourse_id(99999);
		c.setInstructor(TEST_INSTRUCTOR_EMAIL);
		c.setSemester("Fall");
		c.setYear(2021);
		c.setTitle(TEST_COURSE_TITLE);
		
		Assignment a = new Assignment();
		a.setNeedsGrading(1);

		Enrollment e = new Enrollment();
		e.setCourse(c);
		e.setStudentEmail(TEST_USER_EMAIL);
		e.setStudentName(TEST_STUDENT_NAME);
		

		courseRepository.save(c);
		a = assignmentRepository.save(a);
		e = enrollmentRepository.save(e);

		AssignmentGrade ag = null;
	
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);
		
		try {
			driver.findElement(By.id("addAssignment")).click();
			Thread.sleep(SLEEP_DURATION);

			// Automatically set 
			driver.findElement(By.name("course")).sendKeys(Integer.toString(TEST_COURSE_ID));
			driver.findElement(By.name("name")).sendKeys(TEST_ASSIGNMENT_NAME);
			driver.findElement(By.name("DueDate")).sendKeys(TEST_DUE_DATE);
			Thread.sleep(SLEEP_DURATION);
			
			// Automatically click add
			driver.findElement(By.id("addId")).click();
			Thread.sleep(SLEEP_DURATION);
			
			
		} catch (Exception ex) {
			throw ex;
		} finally {

			/*
			 *  clean up database so the test is repeatable.
			 */
		
			enrollmentRepository.delete(e);
			assignmentRepository.delete(a);
			enrollmentRepository.delete(e);
			

			driver.quit();
		}

	}

}
