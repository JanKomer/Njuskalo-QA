package src.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.errors.TestException;

import java.util.InputMismatchException;

/**
 * Class for working with car search results on Njuškalo.
 * Currently only contains a fucntion for entering parameters, specifically production years and max kilometers.
 */
public class NjuskaloCarSearchResultsPage {

    static final Logger logger = LogManager.getLogger(NjuskaloCarSearchResultsPage.class);

    private Page page;

    public NjuskaloCarSearchResultsPage(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    /**
     * Function used to alter search paramaters on the given cars' search result page.
     *
     * @param paramaters
     * Currently accepts paramaters in the form of starting_year-ending_year,max_kilometers entered as a string.
     */
    public void enterSearchParamaters(String paramaters) throws TestException {
            try{
                if(!paramaters.matches("\\d{4}-\\d{4},\\d+")) throw new InputMismatchException("Search paramater format is incorrect.");
                String startingYear = paramaters.split("-")[0];
                String endingYear = paramaters.split(",")[0].split("-")[1];
                String maxKilometers = paramaters.split(",")[1];

                page.getByLabel("Godina proizvodnje").selectOption(startingYear);
                page.locator("[id=\"yearManufactured[max]\"]").selectOption(endingYear);
                page.locator("input[name=\"mileage[max]\"]").click();
                page.locator("input[name=\"mileage[max]\"]").fill(maxKilometers);
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Pronjuškaj")).click();
            } catch (InputMismatchException inputException) {
                throw new TestException(inputException.getMessage());
            } catch (Exception e){
                logger.error("Njuskalo", e);
                throw new TestException(e.getMessage());
            }

    }
}
