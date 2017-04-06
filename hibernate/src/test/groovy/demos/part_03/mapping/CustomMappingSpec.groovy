package demos.part_03.mapping

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import org.grails.orm.hibernate.HibernateDatastore
import org.hibernate.type.YesNoType
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

/**
 * - New MappingBuilder class to static import
 * - Helps IDEs for code completion
 */
import static grails.gorm.hibernate.mapping.MappingBuilder.*

class CustomMappingSpec extends Specification {

    @Shared @AutoCleanup HibernateDatastore datastore = new HibernateDatastore(getClass().getPackage())

    @Rollback
    void "Custom mapping has a better DSL!"() {
        when:"All you do is statically import MappingBuilder"
        new Plant(name: "Banana", hasLeaves: true, leafColor: "Green").save(flush:true)

        then:"You get better completion in the IDE!"
        Plant.count() == 1
    }

}

@Entity
class Plant {
    String name
    boolean hasLeaves
    String leafColor

    static mapping = orm {
        version false
        dynamicUpdate true
        leafColor property {
            nullable(true)
        }
        hasLeaves property {
            type(YesNoType)
        }
    }
}