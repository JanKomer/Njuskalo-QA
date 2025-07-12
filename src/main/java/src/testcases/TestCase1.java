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
        if(args.length != 4) {
            System.out.println("Usage: java -jar testcase.jar <carBrandNjuskalo> <parametersNjuskalo> <carBrandBolha> <parametersBolha>");
            System.out.println("Paramaters of format: <starting_year>-<ending_year>,<max_kilometers>");
            System.exit(1);
        }

        String njuskaloCar = args[0];
        String njuskaloParams = args[1];
        String bolhaCar = args[2];
        String bolhaParams = args[3];


        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));

            BrowserContext njuskaloContext = browser.newContext();
            Page pageNjuskalo = njuskaloContext.newPage();
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
        } catch (TestException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }


    }
}
