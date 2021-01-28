package com.ikea.warehouse;

import com.ikea.warehouse.models.Product;
import com.ikea.warehouse.models.Article;
import com.ikea.warehouse.models.Inventory;
import com.ikea.warehouse.repositories.ProductRepository;
import com.ikea.warehouse.repositories.ArticleRepository;
import com.ikea.warehouse.repositories.InventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WarehouseIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void beforeAllTests() {
        inventoryRepository.deleteAll();
        articleRepository.deleteAll();
        productRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void beforeEachMethod() {
        inventoryRepository.deleteAll();
        articleRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void GivenWarehouseServiceIsUp_WhenPutInventory_ThenReplyOkAndAddOneArticle() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(put("/inventory/add").header("Content-type", "application/json")
                        .content("{\"inventory\":[{\"art_id\":\"1\",\"name\":\"4\",\"stock\":\"4\"}]}"))
                .andExpect(status().isOk()).andReturn();

        assertEquals(null, mvcResult.getResponse().getContentType());
        assertEquals(1, articleRepository.count());
    }

    @Test
    public void GivenWarehouseServiceIsUp_WhenGetProduct_ThenReplyBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/products/all").header("Content-type", "application/json"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertEquals(productRepository.count(), 0);
    }

    @Test
    public void GivenWarehouseServiceIsUp_WhenPutProducts_ThenReplyOkAndAddOneProduct() throws Exception {
        Article article = new Article();
        article.setId(1);
        article.setName("Leg");
        article.setStock(4);
        article = articleRepository.save(article);

        MvcResult mvcResult = mockMvc.perform(put("/products/add").header("Content-type", "application/json").content(
                "{\"products\":[{\"name\":\"Desk\",\"contain_articles\":[{\"art_id\":\"1\",\"amount_of\":\"4\"}]}]}"))
                .andExpect(status().isOk()).andReturn();

        assertEquals(null, mvcResult.getResponse().getContentType());
        assertEquals(productRepository.count(), 1);
    }    

    @Test
    public void GivenArticleInventoryProduct_WhenAdd_ThenReplyWithOneEach() throws Exception {

        Article article = new Article();
        article.setId(1);
        article.setName("Leg");
        article.setStock(4);
        article = articleRepository.save(article);

        Product product = new Product();
        product.setName("Desk");
        product.setPrice(4.0);
        product = productRepository.save(product);

        Inventory inventory = new Inventory(article, product, 4);
        inventory = inventoryRepository.save(inventory);

        assertEquals(1, articleRepository.count());
        assertEquals(1, productRepository.count());
        assertEquals(1, inventoryRepository.count());
    }
}