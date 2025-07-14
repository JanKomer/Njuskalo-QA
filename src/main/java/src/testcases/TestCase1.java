package src.testcases;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import src.errors.TestException;
import src.pages.BolhaCarSearchResultsPage;
import src.pages.BolhaHomePage;
import src.pages.NjuskaloCarSearchResultsPage;
import src.pages.NjuskaloHomePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.util.*;

public class TestCase1 {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Arguments: <carBrandNjuskalo> <parametersNjuskalo> <carBrandBolha> <parametersBolha>");
            System.out.println("Paramaters of format: <starting_year>-<ending_year>,<max_kilometers>");
            System.exit(1);
        }

        String njuskaloCar = args[0];
        String njuskaloParams = args[1];
        String bolhaCar = args[2];
        String bolhaParams = args[3];


        Page pageNjuskalo = null;
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));

            BrowserContext njuskaloContext = browser.newContext();
            pageNjuskalo = njuskaloContext.newPage();
            pageNjuskalo.navigate("https://www.njuskalo.hr/");
            NjuskaloHomePage njuskaloHomePage = new NjuskaloHomePage(pageNjuskalo);
            NjuskaloCarSearchResultsPage njuskaloSearchResults = new NjuskaloCarSearchResultsPage(pageNjuskalo);

            njuskaloHomePage.searchCar(njuskaloCar);
            njuskaloSearchResults.enterSearchParamaters(njuskaloParams);

            BrowserContext bohlaContext = browser.newContext();
            Page pageBolha = bohlaContext.newPage();
            pageBolha.navigate("https://www.bolha.com/");
            BolhaHomePage bolhaHomePage = new BolhaHomePage(pageBolha);
            BolhaCarSearchResultsPage bolhaSearchResults = new BolhaCarSearchResultsPage(pageBolha);

            bolhaHomePage.searchCar(bolhaCar);
            bolhaSearchResults.enterSearchParamaters(bolhaParams);


            //Njuškalo TESTS
            assertThat(pageNjuskalo).hasURL("https://www.njuskalo.hr/auti/" + njuskaloCar.toLowerCase() + "?yearManufactured%5Bmin%5D=" + njuskaloParams.split("-")[0] + "&yearManufactured%5Bmax%5D=" + njuskaloParams.split("-")[1].split(",")[0] + "&mileage%5Bmax%5D=" + njuskaloParams.split("-")[1].split(",")[1]);
            Locator resultsNjuskalo = pageNjuskalo.locator("li.EntityList-item:has-text(\"" + njuskaloCar + "\")");
            int countNJ = resultsNjuskalo.count();
            if(!(countNJ > 0)) System.out.println("No results found on Njuskalo, check with different paramaters.");


            //Bolha TESTS
            assertThat(pageBolha).hasURL("https://www.bolha.com/avto-oglasi/"+ bolhaCar.toLowerCase() + "?yearManufactured%5Bmin%5D=" + bolhaParams.split("-")[0] + "&yearManufactured%5Bmax%5D=" + bolhaParams.split("-")[1].split(",")[0] + "&mileage%5Bmax%5D=" + bolhaParams.split("-")[1].split(",")[1]);
            Locator resultsBolha = pageBolha.locator("li.EntityList-item:has-text(\"" + bolhaCar + "\")");
            int countBO = resultsBolha.count();

            if(!(countBO > 0)) System.out.println("No results found on Bolha, check with different paramaters.");
        } catch (TestException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
