package com.unipi.adouladiris.medicalstorage;

import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;
import java.util.Iterator;


@SpringBootApplication
public class MedicalStorageApplication {

    public static void main(String[] args) {

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        DbResult dbResult = new Select().findOperableEntityByName("Aspirine");
//        DbResult dbResult = new Select().findProduct("Aspirine");
//        DbResult dbResult = new Select().findAllProducts();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        Substance substance = new Substance("Aspirine");
//        Tab tab = new Tab("Precaution");
//        Category category = new Category( "Contraindications" );
//        Item item = new Item("Alkool", "TEST_UPDATED");
//        Tag tag = new Tag("PonokefalosSecondTag");
//
//        Product product = new Product( substance, tab, category, item, tag );
//        new Insert().product(product);
//        new Update().entityByName("Ponokefalos2", tag );
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        DbResult dbResult = new Select().findByTag( "Ponokefalos" );
//        DbResult dbResult = new Insert().product( substance, tab, category, item, tag );
//        Product product = new Select().findProduct("Aspirine").getResult( Product.class );
//        DbResult dbResult = new Delete().deleteProduct(product);
//        DbResult dbResult = new Delete().deleteEntityById(Tab.class, 3);
//        DbResult dbResult = new Update().entityById(Category.class, 3, "Contraindications");
//        DbResult dbResult = new Update().entityByName(Category.class, "Contraindications456", "Contraindications");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        DbResult dbResult = new Select().findAllProducts();
        DbResult dbResult = new Select().findProduct("Aspirine");

        System.out.println("---------------- Demo starts --------------------");
        if ( !dbResult.isEmpty() ){

            System.out.println("TEST class: " + dbResult.getResult().getClass().getSimpleName());
            if( dbResult.getResult().getClass().getSimpleName().equals("TreeSet") ) {

                Iterable iterableObject = dbResult.getResult( Iterable.class );
                System.out.println("iterableObject size: " + ((Collection<?>) iterableObject).size());
                Iterator it = iterableObject.iterator();

                while (it.hasNext()) {

                    Object object = it.next();
                    if (object.getClass().getSimpleName().equals("Product")) {
                        Product p = (Product) object;
                        p.printProduct();
                    }

                    System.out.println("End");
                }
            }
            else{

                if(dbResult.getResult().getClass().getSimpleName().equals("Product")){

                    dbResult.getResult( Product.class ).printProduct();
                }
                else if(dbResult.getResult().getClass().getSimpleName().equals("Substance")){
                    Substance substanceId = dbResult.getResult( Substance.class );
                    System.out.println(substanceId.getName() + " ID: " + substanceId.getId() );
                }
                else{
                    System.out.println("Insertion: " + dbResult.getResult() );
                }

            }
        }
        else{
            System.out.println("Empty result!");

            if( dbResult.getException() != null ){
                Exception ex = dbResult.getException();
                System.out.println("--------------------------------------------------");
//                System.out.println(ex.getCause().getMessage() );
                System.out.println("--------------------------------------------------");
//                System.out.println("demo ex: " + ex.getCause().getCause().getLocalizedMessage() );
            }
        }
        System.out.println("----------------  Demo ends  --------------------");

        SpringApplication.run(MedicalStorageApplication.class, args);
    }

}
