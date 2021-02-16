package com.example.stock.stock.controller;

import com.example.stock.stock.StockApplication;
import com.example.stock.stock.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = StockApplication.class
)
@AutoConfigureMockMvc
class ItemControllerIntTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private static int itemCounter = 0;
    private static boolean started = false;
    private static boolean removed = false;

    private static final long firstItemAmount = 2;
    private static final long secondItemAmount = 6;

    private static final Item firstItem = new Item(itemCounter, "Coffee", firstItemAmount, "as124");
    private static String firstItemJson;

    private static final Item secondItem = new Item(itemCounter, "Car", secondItemAmount, "23sf");
    private static String secondItemJson;


    @Autowired
    private MockMvc mockMvc;

    public void populateDb() throws Exception {
        itemCounter ++;
        firstItem.setItemNo(itemCounter);
        firstItemJson = mapper.writeValueAsString(firstItem);
        mockMvc.perform(post("/addItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstItemJson));
    }

    /**
     * This test populate the DB with one item and checks if it returns
     * using the /inventory api to get all the data.
     */
    @Test
    void getInventory() throws Exception {
        // Tried @Before annotations and wont work.
        // After checking the web for possible solutions this was the best option.
        if(!started || removed) {
            populateDb();
            started = true;
            removed = false;
        }

        //Act
        RequestBuilder request = get("/inventory");
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        assertEquals(mapper.readTree("[" + firstItemJson + "]"), mapper.readTree(result.getResponse().getContentAsString()));

    }

    /**
     * Testing the /getItem/{itemNo} api.
     * After inserting one item to the DB the api tries to query the same item.
     */
    @Test
    void getItem() throws Exception {
        if(!started || removed) {
            populateDb();
            started = true;
            removed = false;
        }

        //Act
        RequestBuilder request = get("/getItem/1");
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        assertEquals(mapper.readTree(firstItemJson ), mapper.readTree(result.getResponse().getContentAsString()));

    }

    /**
     * Testing the /addItem api.
     * Using post request sending the json string of the new item.
     * Asserting by catching the response after making the request.
     */
    @Test
    void addItem() throws Exception {
        //Arrange
        itemCounter ++;
        secondItem.setItemNo(itemCounter);
        secondItemJson = mapper.writeValueAsString(secondItem);
        //Act
        RequestBuilder request = post("/addItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondItemJson);
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert
        assertEquals(mapper.readTree(secondItemJson ), mapper.readTree(result.getResponse().getContentAsString()));

    }

    /**
     * Testing the /removeItem/{itemNo} api.
     * Removing one item from the db using the api and expect to get OK.
     */
    @Test
    void removeItem() throws Exception {
        //Arrange
        //Make sure that item 1 exists in the db
        if(!started && !removed) {
            populateDb();
            started = true;
            removed = true;
        }

        //Act
        RequestBuilder request = delete("/removeItem/1");
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        assertEquals("\"OK\"", result.getResponse().getContentAsString());

    }

    /**
     * Testing the /deposit api.
     * Deposit amount of 5 to the database using the item No.
     * Checks the amount after the request.
     */
    @Test
    void deposit() throws Exception {
        if(!started || removed) {
            populateDb();
            started = true;
            removed = false;
        }
        //Arrange
        //The amounts hard coded can be changed to dynamically calculated values.
        long amount = 5;
        long expectedAmount = 0;

        switch (itemCounter){
            case 1: expectedAmount = firstItem.getAmount() + amount;
                    firstItem.setAmount(expectedAmount);
                break;
            case 2: expectedAmount = secondItem.getAmount() + amount;
                    secondItem.setAmount(expectedAmount);
                break;
        }

        //Act
        RequestBuilder request = put("/deposit/" + String.format("%d/%d", itemCounter, amount));
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        int newAmount = parseInt(String.valueOf(mapper.readTree(result.getResponse().getContentAsString()).get("amount")));
        assertEquals(expectedAmount, newAmount);
    }

    /**
     * Testing the /withdraw api.
     * Withdraw amount from one of the items in the DB.
     */
    @Test
    void withdraw() throws Exception{
        if(!started || removed) {
            populateDb();
            started = true;
            removed = false;
        }
        //Arrange
        //The amounts hard coded can be changed to dynamically calculated values.
        long amount = 1;
        long expectedAmount = 0;

        switch (itemCounter){
            case 1: expectedAmount = firstItem.getAmount() - amount;
                    firstItem.setAmount(expectedAmount);
                break;
            case 2: expectedAmount = secondItem.getAmount() - amount;
                    secondItem.setAmount(expectedAmount);
                break;
        }

        //Act
        RequestBuilder request = put("/withdraw/" + String.format("%d/%d", itemCounter, amount));
        MvcResult result = mockMvc.perform(request).andReturn();
        //Assert
        int newAmount = parseInt(String.valueOf(mapper.readTree(result.getResponse().getContentAsString()).get("amount")));
        assertEquals(expectedAmount, newAmount);
    }

}