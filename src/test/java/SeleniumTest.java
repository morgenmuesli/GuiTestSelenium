import net.bytebuddy.dynamic.scaffold.TypeInitializer;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTest {

    private FirefoxDriver driver = null;
    private String ROOTURL = "https://campusonline.uni-ulm.de/qislsf/rds?state=user&type=0";

    /**
     * starts a session before all tests
     */
    @BeforeEach
    public void setUp() {
        driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(930,1140));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(ROOTURL);
    }

    @AfterEach
    public void cleanUp() {
        driver.quit();
    }



    public WebElement getTopbar(){
        return driver.findElement(
                new By.ByXPath("//div[@class='divlinks']"));
    }

    public void checkIfElementContainsEntries(WebElement element, String[] entries)
    {
        for (var entry:
                entries) {
            Assertions.assertNotNull(
                    element.findElement(new By.ByXPath(
                            String.format("//a[contains(text(),'%s')]",
                                    entry)
                    )),
                    String.format("%s is not in element", entry)
            );

        }
    }


    /**
     * test if topmenu consist desired entries
     */
    @Test
    public void testTopMenu(){
        String[] desiredElements = {"Studentisches Leben",
                "Veranstaltungen",
                "Organisationseinheiten",
                "Studium",
                "Räume und Gebäude",
                "Personen"
        };
        var topmenu = getTopbar();
        checkIfElementContainsEntries(
                topmenu,
                desiredElements
        );

    }

    /**
     * test if topmenu appears in english
     */
    @Test
    public void testEnglish() {
        String[] desiredElements = {"Student life",
                "Courses",
                "Orgunits",
                "Study",
                "Facilities",
                "Member"
        };

        driver.findElement(new By.ByXPath(
                "//img[@alt='Switch to english language']/ancestor::a")
        ).click();

        var topmenu = getTopbar();
        checkIfElementContainsEntries(
                topmenu,
                desiredElements
        );
    }

    @Test
    public void testSearchingForCourse() throws InterruptedException {
        driver.findElement(
                new By.ByLinkText("Veranstaltungen")
        ).click();

        driver.findElement(
                new By.ByLinkText("Suche nach Veranstaltungen")
        ).click();

        driver.findElement(By.id("veranstaltung.dtxt")).sendKeys("Softwarequalitätssicherung");

        driver.findElement(By.id("veranstaltung.dtxt")).sendKeys(Keys.ENTER);

        var result = driver.findElement(
                                new By.ByXPath(
                                        "//td/a[contains(text(),'Softwarequalitätssicherung')]"
                                ));



        Assertions.assertNotNull(result, "Table doesn't contain Softwarequalitätssicherung");

        result.click();



        var list = result.findElement(new By.ByCssSelector(".mod_n"));

        Assertions.assertNotNull(list.findElement(new By.ByXPath("//*[contains(text(),'CS7251.000')]")), "ID isn't in table");






    }
}
