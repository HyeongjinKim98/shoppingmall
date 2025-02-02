package com.example.shoppingmall.service.Impl;

import com.example.shoppingmall.dao.ProductDAO;
import com.example.shoppingmall.dao.UserDAO;
import com.example.shoppingmall.data.dto.request.*;
import com.example.shoppingmall.data.dto.response.ResponseProduct;
import com.example.shoppingmall.data.dto.response.ResponseProductSummary;
import com.example.shoppingmall.data.entity.Product;
import com.example.shoppingmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    @Autowired
    public ProductServiceImpl(ProductDAO productDAO, UserDAO userDAO){
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    @Override
    public List<ResponseProductSummary> mainPageProductList() {
        return null;
    }

    @Override
    public List<ResponseProductSummary> findByProductName(String keyword) {
        // Dto -> Entity
        List<Product> productList = productDAO.findByProductName(keyword);
        if (productList == null){
            return null;
        }

        // Entity -> Dto
        List<ResponseProductSummary> responseProductList = new ArrayList<>();
        for(Product product : productList){
            ResponseProductSummary newDto = new ResponseProductSummary();
            newDto.setId(product.getId());
            newDto.setName(product.getName());
            newDto.setPrice(product.getPrice());
            newDto.setImgKey(product.getImgKey());

            responseProductList.add(newDto);
        }
        return responseProductList;
    }

    @Override
    public List<ResponseProductSummary> findAllProduct() {
        // Dto -> Entity
        List<Product> productList = productDAO.findAllProduct();
        if (productList == null){
            return null;
        }

        // Entity -> Dto
        List<ResponseProductSummary> responseProductList = new ArrayList<>();
        for(Product product : productList){
            ResponseProductSummary newDto = new ResponseProductSummary();
            newDto.setId(product.getId());
            newDto.setName(product.getName());
            newDto.setPrice(product.getPrice());
            newDto.setImgKey(product.getImgKey());

            responseProductList.add(newDto);
        }

        return responseProductList;
    }

    @Override
    public List<ResponseProductSummary> findByCategory(String category) {
        // Dto -> Entity
        List<Product> productList = productDAO.findByCategory(category);
        if (productList == null){
            return null;
        }

        // Entity -> Dto
        List<ResponseProductSummary> responseProductList = new ArrayList<>();
        for(Product product : productList){
            ResponseProductSummary newDto = new ResponseProductSummary();
            newDto.setId(product.getId());
            newDto.setName(product.getName());
            newDto.setPrice(product.getPrice());
            newDto.setImgKey(product.getImgKey());

            responseProductList.add(newDto);
        }
        return responseProductList;
    }

    @Override
    public ResponseProduct findById(Long id) {
        // Dto -> Entity
        Product product = productDAO.findById(id);
        if (product == null){
            return null;
        }

        // Entity -> Dto
        ResponseProduct responseProduct = new ResponseProduct();
        responseProduct.setId(product.getId());
        responseProduct.setName(product.getName());
        responseProduct.setPrice(product.getPrice());
        responseProduct.setCategory(product.getCategory());
        responseProduct.setDescription(product.getDescription());
        responseProduct.setSize(product.getSize());
        responseProduct.setImgKey(product.getImgKey());
        return responseProduct;
    }

    @Override
    public List<ResponseProductSummary> findByUsername(String username) {
        // Dto -> Entity
        List<Product> productList = productDAO.findByUsername(username);
        if (productList == null){
            return null;
        }

        // Entity -> Dto
        List<ResponseProductSummary> responseProductList = new ArrayList<>();
        for(Product product : productList){
            ResponseProductSummary newDto = new ResponseProductSummary();
            newDto.setId(product.getId());
            newDto.setName(product.getName());
            newDto.setPrice(product.getPrice());
            newDto.setImgKey(product.getImgKey());

            responseProductList.add(newDto);
        }
        return responseProductList;
    }

    @Override
    public ResponseProduct CreateProduct(RequestProduct requestProduct){
        // Dto -> Entity
        Product product = new Product();
        product.setName(requestProduct.getName());
        product.setPrice(requestProduct.getPrice());
        product.setCategory(requestProduct.getCategory());
        product.setDescription(requestProduct.getDescription());
        product.setSize(requestProduct.getSize());
        product.setImgKey(requestProduct.getImgKey());
        product.setUser(userDAO.findByUsername(requestProduct.getUsername()));
//        product.setCartList(null);
//        product.setOrderProductList(null);
        Product createdProduct = productDAO.createProduct(product);

        // Entity -> Dto
        ResponseProduct responseProduct = new ResponseProduct();
        responseProduct.setId(createdProduct.getId());
        responseProduct.setName(createdProduct.getName());
        responseProduct.setPrice(createdProduct.getPrice());
        responseProduct.setCategory(createdProduct.getCategory());
        responseProduct.setDescription(createdProduct.getDescription());
        responseProduct.setSize(createdProduct.getSize());
        responseProduct.setImgKey(createdProduct.getImgKey());
        return responseProduct;
    }

    @Override
    public ResponseProduct editProduct(Long id, String username) {
        // Dto -> Entity
        Product product = productDAO.findById(id);

        // Entity -> Dto
        if (product.getUser().getUsername().equals(username)) {
            ResponseProduct responseProduct = new ResponseProduct();
            responseProduct.setId(product.getId());
            responseProduct.setName(product.getName());
            responseProduct.setPrice(product.getPrice());
            responseProduct.setCategory(product.getCategory());
            responseProduct.setDescription(product.getDescription());
            responseProduct.setSize(product.getSize());
            responseProduct.setImgKey(product.getImgKey());
            return responseProduct;
        } else {
            return null;
        }
    }

    @Override
    public ResponseProduct updateProduct(RequestProductModify requestProductModify) {
        // Dto -> Entity
        Product product = productDAO.findById(requestProductModify.getId());
        product.setName(requestProductModify.getName());
        product.setPrice(requestProductModify.getPrice());
        product.setCategory(requestProductModify.getCategory());
        product.setDescription(requestProductModify.getDescription());
        product.setSize(requestProductModify.getSize());
        product.setImgKey(requestProductModify.getImgKey());
        Product modified_Product = productDAO.updateProduct(product);

        // Entity -> Dto
        ResponseProduct responseProduct = new ResponseProduct();
        responseProduct.setId(modified_Product.getId());
        responseProduct.setName(modified_Product.getName());
        responseProduct.setPrice(modified_Product.getPrice());
        responseProduct.setCategory(modified_Product.getCategory());
        responseProduct.setDescription(modified_Product.getDescription());
        responseProduct.setSize(modified_Product.getSize());
        responseProduct.setImgKey(modified_Product.getImgKey());
        return responseProduct;
    }

    @Override
    public boolean deleteProduct(Long id, String username) {
        // Dto -> Entity
        Product product = productDAO.findById(id);

        // Entity -> Dto
        if (product.getUser().getUsername().equals(username)) {
            productDAO.deleteProduct(product);
            return true;
        } else {
            return false;
        }
    }

}
