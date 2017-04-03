package demos.part_01.transactional

import grails.gorm.annotation.Entity
import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Rollback
import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.core.connections.ConnectionSource
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification


/**
 * New annotations in the grails.gorm.transactions package
 *
 * - @Transactional
 * - @ReadOnly
 * - @NotTransactional
 * - @Rollback
 */
class TransactionAnnotationSpec extends Specification {

    @Shared @AutoCleanup HibernateDatastore datastore =
            new HibernateDatastore(getClass().getPackage())

    @Rollback
    void "New @Transactional and @ReadOnly annotations!"() {
        when:"Look no need to do any Spring config for them to work!"
        BookService bookService = new BookService()
        bookService.saveBook("The Stand")

        then:"Also possible to specify the connection!"
        bookService.find("The Stand") != null
    }

}

@Entity
class Book {
    String title
}

@Transactional
class BookService {
    void saveBook(String title) {
        new Book(title: title).save()
    }

    @ReadOnly(connection = ConnectionSource.DEFAULT)
    Book find(String title) {
        Book.findByTitle(title)
    }
}
