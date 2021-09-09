package com.zendesk.challenge;

import com.zendesk.challenge.domain.commons.Entity;
import com.zendesk.challenge.exception.ExitException;
import com.zendesk.challenge.exception.UserInputException;
import com.zendesk.challenge.io.IOService;
import com.zendesk.challenge.io.UserInput;
import com.zendesk.challenge.search.DataIndexingService;
import com.zendesk.challenge.search.DataInitializerService;
import com.zendesk.challenge.search.Index;
import com.zendesk.challenge.search.SearchService;

import java.util.List;
import java.util.Set;

public class App {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX"; //ideally this, along with other constants would go into a config file.

    private static DataInitializerService dataInitializerService = new DataInitializerService();
    private static DataIndexingService dataIndexingService = new DataIndexingService();

    public static void main(String[] args) {

        List<Index<? extends Entity>> indices = dataInitializerService.initializeIndices();
        indices.forEach(dataIndexingService::generateIndexes);
        SearchService searchService = new SearchService(indices);
        IOService ioService = new IOService(System.in, System.out, indices);


        while (true) {
            try {
                UserInput input = ioService.getNext();
                Set<? extends Entity> results = searchService.search(input, true);
                ioService.displayResult(input, results);
            } catch (UserInputException ex) {
                System.err.print(ex.getMessage());
            } catch (ExitException ex) {
                System.exit(0);
            }
        }

    }

}
