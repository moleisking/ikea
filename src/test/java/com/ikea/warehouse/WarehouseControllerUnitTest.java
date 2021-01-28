package com.ikea.warehouse;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.ikea.warehouse.controller.WarehouseController;
import com.ikea.warehouse.repositories.ProductRepository;
import com.ikea.warehouse.repositories.ArticleRepository;
import com.ikea.warehouse.repositories.InventoryRepository;
import com.ikea.warehouse.services.WarehouseService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WarehouseControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleRepository articleRepository;
    
    @MockBean
    private ProductRepository carRepository;

    @Mock
    private WarehouseController journeyController;    

    @MockBean
    private WarehouseService warehouseService;   

    @Test
    public void GivenWarehouseServiceIsUp_WhenPutInventory_ThenReplyOk() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(put("/inventory/add").header("Content-type", "application/json")
                        .content("{\"inventory\"[{\"art_id\":\"1\",\"name\":\"4\",\"stock\":\"4\"}]}"))
                .andExpect(status().isOk()).andReturn();
        assertEquals(null, mvcResult.getResponse().getContentType());
    }

    @Test
    public void GivenWarehouseServiceIsUp_WhenDeleteProduct_ThenReplyOk() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(post("/product/remove").header("Content-type", "application/json").content("{\"name\":\"Desk\",\"contain_articles\":[{\"art_id\":\"4\",\"amount_of\":\"4\"}]}"))
                .andExpect(status().isOk()).andReturn();
        assertEquals(null, mvcResult.getResponse().getContentType());
    }

    @Test
    public void GivenWarehouseServiceIsUp_WhenGetProducts_ThenReplyOk() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(get("/products/all").header("Content-type", "application/json"))
                .andExpect(status().isOk()).andReturn();
        assertEquals(null, mvcResult.getResponse().getContentType());
    }

    @Test
    public void GivenWarehouseServiceIsUp_WhenPutProducts_ThenReplyOk() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(put("/products/add").header("Content-type", "application/json")
                        .content("{\"products\"[{\"name\":\"Desk\",\"contain_articles\":[{\"art_id\":\"4\",\"amount_of\":\"4\"}]}]}"))
                .andExpect(status().isOk()).andReturn();
        assertEquals(null, mvcResult.getResponse().getContentType());
    }     

    @Test
    public void GivenWarehouseServiceIsUp_WhenGetStatus_ThenReplyOk() throws Exception {
        mockMvc.perform(get("/status")).andExpect(status().isOk()).andReturn();
    }
}