package com.unipi.adouladiris.medicalstorage.database.session;

import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTab;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategory;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItem;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItemTag;
import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.*;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

// Class containing static information about jpa api.
// All dao classes extend DbEntitySessionManager class to have access to jpa and open a new static session if there is no session.
public abstract class DbEntitySessionManager {
    protected static EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("javax.persistence.medical_storage.jpa");
    protected static SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    protected static Session session = sessionFactory.openSession();

    // Count entities at join tables. If entity reference (as a foreign key) to a join table is removed and is not attached to a join table,
    // then delete it from the corresponding table (Deletion is executed at updateJoinTable method, not here).
    protected DbResult getRecordsCount(Class<? extends Joinable> joinTableName, Class<? extends Operable> operableName, String oldKey){

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("FROM ");

        if(joinTableName.equals(SubstanceTab.class)){
            queryBuilder.append("SubstanceTab jt ");
        }
        else if(joinTableName.equals(SubstanceTabCategory.class)){
            queryBuilder.append("SubstanceTabCategory jt ");
        }
        else if(joinTableName.equals(SubstanceTabCategoryItem.class)){
            queryBuilder.append("SubstanceTabCategoryItem jt ");
        }
        else if(joinTableName.equals(SubstanceTabCategoryItemTag.class)){
            queryBuilder.append("SubstanceTabCategoryItemTag jt ");
        }

        queryBuilder.append("INNER JOIN ");

        if(operableName.equals(Substance.class)){
            queryBuilder.append("jt.substance WHERE jt.substance.name =: name ");
        }
        else if(operableName.equals(Tab.class)){
            queryBuilder.append("jt.tab WHERE jt.tab.name =: name ");
        }
        else if(operableName.equals(Category.class)){
            queryBuilder.append("jt.category WHERE jt.category.name =: name ");
        }
        else if(operableName.equals(Item.class)){
            queryBuilder.append("jt.item WHERE jt.item.name =: name ");
        }
        else if(operableName.equals(Tag.class)){
            queryBuilder.append("jt.tag WHERE jt.tag.name =: name ");
        }

        String select = queryBuilder.toString();
        Query query = session.createQuery(select);
        query.setParameter("name", oldKey);
        List<Object[]> queryResultList = query.getResultList();
        return new DbResult(queryResultList.size());
    }

}
