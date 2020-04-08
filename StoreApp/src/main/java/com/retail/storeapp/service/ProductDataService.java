package com.retail.storeapp.service;


import com.retail.storeapp.entity.Product;
import com.retail.storeapp.repository.ProductRepository;
import com.retail.storeapp.repository.RequestSpecificationCriteria;
import com.retail.storeapp.repository.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("productService")
public class ProductDataService {

    private static final Logger logger = LoggerFactory.getLogger(ProductDataService.class);

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("greeting")
    public String greeting(){
        return "This is productService";
    }

    @PostMapping("addProduct")
    public String addProduct(@RequestBody Product product){
        try {
            product.setLastUpdatedDate(new Date());
            Optional<Product> productOptionalObj = Optional.ofNullable(productRepository.save(product));
            return "Product saved with id-"+productOptionalObj
                    .map(Product::getProductId)
                    .map(String::valueOf)
                    .orElse("Unable to fetch id of saved product");
        } catch (Exception e) {
            logger.error("Exception occurred while saving product object", e);
        }
        return "Unable to add product";
    }


    @GetMapping("deleteProduct")
    public String deleteProduct(@RequestParam Integer productId){
        try {
            List<Product> products = findProduct(productId, null, null);
            Product fetchedProduct = products.size() > 0 ? products.get(0) : null;
            if(fetchedProduct == null)
                return "Unable to find product";
            fetchedProduct.setActive(false);
            fetchedProduct.setLastUpdatedDate(new Date());
            updateProduct(fetchedProduct);
        } catch (Exception e) {
            logger.error("Exception occurred while deleting product object", e);
            return "Exception occurred while deleteing product";
        }
        return "Product deleted successfully";
    }

    @PostMapping("updateProduct")
    public String updateProduct(@RequestBody Product product){
        try {
            return addProduct(product);
        } catch (Exception e) {
            logger.error("Exception occurred while updating product object", e);
        }
        return "Unable to update product";
    }

    @GetMapping("findProduct")
    public List<Product> findProduct(
            @RequestParam Integer productId,
            @RequestParam String productName,
            @RequestParam String productBarCode) {
        List<Product> products = new ArrayList<>();
        Specification specification = null;

        if (!StringUtils.isEmpty(productName)) {
            RequestSpecificationCriteria requestSpecificationCriteria
                    = new RequestSpecificationCriteria(new SearchCriteria("productName", "STRING:=LIKE", productName.toLowerCase()));
            specification = Specification.where(requestSpecificationCriteria);
        }

        if (!StringUtils.isEmpty(productBarCode)) {
            RequestSpecificationCriteria requestSpecificationCriteria
                    = new RequestSpecificationCriteria(new SearchCriteria("productBarCode", "STRING:=LIKE", productBarCode));
            if (specification == null)
                specification = Specification.where(requestSpecificationCriteria);
            else
                specification = specification.and(requestSpecificationCriteria);
        }

        if (!StringUtils.isEmpty(productId)) {
            RequestSpecificationCriteria requestSpecificationCriteria
                    = new RequestSpecificationCriteria(new SearchCriteria("productId", "STRING:=", productId));
            if (specification == null)
                specification = Specification.where(requestSpecificationCriteria);
            else
                specification = specification.and(requestSpecificationCriteria);
        }

        products = productRepository.findAll(specification);
        return products;
    }


    @GetMapping("getAllProducts")
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Specification specification = null;

        RequestSpecificationCriteria requestSpecificationCriteria
                = new RequestSpecificationCriteria(new SearchCriteria("active", "BOOLEAN:=TRUE", "TRUE"));
        specification = Specification.where(requestSpecificationCriteria);

        products = productRepository.findAll(specification);
        return products;
    }

    @GetMapping("findByBarcode")
    public List<Product> findByBarcode(@RequestParam String productBarCode){
        return productRepository.findByProductBarCode(productBarCode);
    }
}
