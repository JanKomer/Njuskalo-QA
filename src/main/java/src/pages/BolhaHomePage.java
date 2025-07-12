package src.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.errors.TestException;

import java.util.Arrays;

/**
 * Class which contains functions for searching the Bolha online marketplace.
 * Currently only use case is car brands.
 */
public class BolhaHomePage {
    static final Logger logger = LogManager.getLogger(BolhaHomePage.class);

    private Page page;

    public BolhaHomePage(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    /**
     * Function for searching a car brand on the Bolha home page.
     * @param carBrandName
     * A string with the name of the car brand.
     */
    public void searchCar(String carBrandName) throws TestException {
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sprejmi vse in zapri")).click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Razumem")).click();
            page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Kaj iščete danes?")).click();
            page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Kaj iščete danes?")).fill(carBrandName);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Kategorije")).click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("BMW").setExact(true)).locator("span").click();
        } catch (TimeoutError error){
            logger.error("Bolha", error);
            throw new TestException("Search timed out, please check if car brand is correct.");
        } catch (Exception e){
            logger.error("Bolha", e);
            throw new TestException(e.getMessage());
        }
    }
}
