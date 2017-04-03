package demos.part_04.hql

import grails.gorm.annotation.Entity
import grails.gorm.hibernate.HibernateEntity
import grails.gorm.transactions.Rollback
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

/**
 * - All GORM methods now take CharSequene instead of String
 * - If the object is a GString we apply automatic named parameters
 * - Works for the new SQL query support too
 */
class HqlQuerySpec extends Specification {
    @Shared @AutoCleanup HibernateDatastore datastore = new HibernateDatastore(getClass().getPackage())

    @Rollback
    void "HQL and SQL query methods now have versions that escape GStrings!"() {

        given:
        String name = "MacBook"
        new Product(name: "iPod").save(flush:true)
        new Product(name: name).save(flush:true)

        expect: "Look how you can use the HibernateEntity trait to provide a findWithSql method!"
        Product.find("from Product p where p.name = $name").name == "MacBook"
        Product.findWithSql("select * from product where name = $name").name == "MacBook"
    }
}

@Entity
class Product implements HibernateEntity<Product> {
    String name
    Date dateCreated
}
