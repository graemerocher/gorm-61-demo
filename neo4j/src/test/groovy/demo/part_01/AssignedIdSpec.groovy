package demo.part_01

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import org.grails.datastore.gorm.neo4j.Neo4jDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static grails.neo4j.mapping.MappingBuilder.*

/**
 * - Package scanning supported
 * - Easy unit testing supported
 * - Assigned identifiers now supported in Neo4j
 * - Batch inserts with UNWIND
 */
class AssignedIdSpec extends Specification {
    @Shared @AutoCleanup Neo4jDatastore datastore = new Neo4jDatastore(getClass().getPackage())


    @Rollback
    def "You can now use assigned identifiers in Neo4j"() {
        when:
        new Plant(name: "Banana").save()
        new Plant(name: "Apple").save()
        new Plant(name: "Oak Tree").save()
        new Plant(name: "Grape Vine").save(flush:true)

        then:
        Plant.count == 4

    }
}

@Entity
class Plant {
    String name

    static mapping = node {
        id generator:'assigned', name:'name'
    }
}
