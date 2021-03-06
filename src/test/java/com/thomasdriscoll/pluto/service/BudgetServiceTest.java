package com.thomasdriscoll.pluto.service;

import com.thomasdriscoll.pluto.lib.dao.BudgetRepo;
import com.thomasdriscoll.pluto.lib.exceptions.DriscollException;
import com.thomasdriscoll.pluto.lib.exceptions.BudgetExceptionEnums;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BudgetServiceTest {
    //Complete dummy variable but just wanted to highlight that you'd probably have one of these in your service tests
    @MockBean
    private BudgetRepo budgetRepo;

    @Autowired
    private BudgetService budgetService;

    @Nested
    @DisplayName("Dummy function service tests")
    class DummyFunctionServiceTests{
        //Variables used for testing this function specifically
        private final String name = "Thomas";
        private final String badName = "Thummus";
        private final String nameResponse = "My name is Thomas";

        @Test
        public void whenValidName_returnNameWithMessage() throws DriscollException {
            String actual = budgetService.dummyFunction(name);
            assertEquals(nameResponse, actual);
        }

        @Test
        public void whenInvalidName_throwException() throws DriscollException {
            DriscollException excepted = new DriscollException(BudgetExceptionEnums.TESTING_EXCEPTIONS.getStatus(), BudgetExceptionEnums.TESTING_EXCEPTIONS.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> budgetService.dummyFunction(badName));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }
    }
}