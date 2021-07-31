package com.unipi.adouladiris.medicalstorage.rest.controller;
import com.unipi.adouladiris.medicalstorage.business.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.delete.Delete;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.rest.controller.abstractClass.RoutingController;
import com.unipi.adouladiris.medicalstorage.rest.dto.DataTransferObject;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@RestController
public class ProductController extends RoutingController {

    @GetMapping("/product/all")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    public ResponseEntity<String> getAllProducts() {
        DbResult dbResult = new Select().findAllProducts();
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        TreeSet<Product> productSet = dbResult.getResult(TreeSet.class);
        DataTransferObject dto = new DataTransferObject(productSet);
        return new ResponseEntity(dto.getJsonSet(), HttpStatus.OK);
    }

    @GetMapping("/product/{name}")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    public ResponseEntity<String> getProduct(@PathVariable String name) {
        DbResult dbResult = new Select().findProduct(name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        DataTransferObject dto = new DataTransferObject(product);
        return new ResponseEntity(dto.getJsonSet(), HttpStatus.OK);
    }

    @DeleteMapping("/product/{name}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteProduct(@PathVariable String name) {
        DbResult dbResult = new Delete().deleteEntityByName(Substance.class, name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        if(dbResult.getException() != null) {
            return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity(dbResult.getResult(), HttpStatus.OK);
    }

    @PostMapping("/product/")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> insertProduct(@RequestBody ArrayList<Object> body) {
        Set<Product> productSet = new DataTransferObject(body).getProductSet();
        Map<String, Integer> results = new HashMap();
        for (Product p : productSet){
            DbResult dbResult = new Insert().product(p);
            HashMap<String, Integer> resultMap =  dbResult.getResult(HashMap.class);
            results = resultMap;
        }

        //TODO review response format
//        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        return new ResponseEntity(results.toString(), HttpStatus.OK);

    }

    // TODO needs fix. Not working.
    @PutMapping("/product/{name}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> updateProduct(@PathVariable String name22, @RequestBody String name) {

        DbResult dbResult = new Select().findProduct(name);

        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        return new ResponseEntity(HttpStatus.OK);
    }

//    @WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
//    public class LogoutServlet extends HttpServlet {
//        @Override
//        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//            HttpSession session = request.getSession(false);
//            // Destroys the session for this user.
//            if (session != null)
//                session.invalidate();
//            // Redirects back to the initial page.
//            response.sendRedirect(request.getContextPath());
//        }
//    }




}
