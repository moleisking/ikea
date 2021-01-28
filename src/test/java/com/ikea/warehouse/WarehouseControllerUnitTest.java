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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WarehouseControllerUnitTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ArticleRepository articleRepository;

        @MockBean
        private InventoryRepository inventoryRepository;

        @MockBean
        private ProductRepository productRepository;

        @Mock
        private WarehouseController warehouseController;

        @MockBean
        private WarehouseService warehouseService;

        @Test
        public void GivenWarehouseServiceIsUp_WhenPutInventory_ThenReplyOk() throws Exception {

                MvcResult mvcResult = mockMvc.perform(put("/inventory/add").header("Content-type", "application/json")
                                .content("{\"inventory\":[{\"art_id\":\"1\",\"name\":\"leg\",\"stock\":\"4\"}]}"))
                                .andExpect(status().isOk()).andReturn();

                assertEquals(null, mvcResult.getResponse().getContentType());
        }

        @Test
        public void GivenWarehouseServiceIsUp_WhenDeleteProduct_ThenReplyOk() throws Exception {

                when(warehouseService.removeProduct(any())).thenReturn(true);

                MvcResult mvcResult = mockMvc.perform(delete("/product/remove")
                                .header("Content-type", "application/json").content("{\"name\":\"Desk\"}"))
                                .andExpect(status().isOk()).andReturn();

                assertEquals(null, mvcResult.getResponse().getContentType());
        }

        @Test
        public void GivenWarehouseServiceIsUp_WhenGetProducts_ThenReplyOk() throws Exception {

                MvcResult mvcResult = mockMvc.perform(get("/products/all").header("Content-type", "application/json"))
                                .andExpect(status().isOk()).andReturn();

                assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        public void GivenWarehouseServiceIsUp_WhenPutProducts_ThenReplyOk() throws Exception {

                MvcResult mvcResult = mockMvc.perform(put("/products/add").header("Content-type", "application/json")
                                .content("{\"products\":[{\"name\":\"Desk\",\"contain_articles\":[{\"art_id\":\"1\",\"amount_of\":\"4\"}]}]}"))
                                .andExpect(status().isOk()).andReturn();

                assertEquals(null, mvcResult.getResponse().getContentType());
        }

}