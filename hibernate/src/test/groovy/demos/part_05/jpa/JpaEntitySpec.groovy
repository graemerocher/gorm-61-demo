package demos.part_05.jpa

import grails.gorm.transactions.Rollback
import org.grails.datastore.gorm.GormEntity
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * - JPA entities (written in Groovy) supported again
 * - Classes annotated with javax.persistence.Entity transformed
 * - If you prefer the annotation approach you can use it instead!
 *
 */
class JpaEntitySpec extends Specification {

    @Shared @AutoCleanup HibernateDatastore datastore = new HibernateDatastore(getClass().getPackage())

    @Rollback
    void "JPA works again!"() {
        when:"To make the GORM methods appear on a JPA entity implement GormEntity"
        new Animal(name: "Frog").save(flush:true)

        then:
        Animal.count() == 1
    }
}

@Entity
class Animal implements GormEntity<Animal> {
    @Id @GeneratedValue Long id
    String name
    int legs = 4
}
