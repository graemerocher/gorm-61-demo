package demo.part_02

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.neo4j.Node
import grails.neo4j.Relationship
import org.grails.datastore.gorm.neo4j.Neo4jDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

/**
 * - You can now map entities to relationships as well as Neo4j nodes
 * - GORM queries supported on relationships
 */
class RelationshipsSpec extends Specification {
    @Shared @AutoCleanup Neo4jDatastore datastore = new Neo4jDatastore(getClass().getPackage())

    @Rollback
    def "You can define entities that represent the relationship between two entities"() {

        given:
        def theMatrix = new Movie(title: "The Matrix").save()
        new Appearance(
            from: new Person(name: "Keanu Reeves"),
            to: theMatrix,
            roles: ['Neo', "Thomas Anderson"]
        ).save()

        new Appearance(
                from: new Person(name: "Carrie Anne Moss"),
                to: theMatrix,
                roles: ['Trinity']
        ).save()

        when:"The relationship is queries"
        Appearance appearance = Appearance.first()


        then:
        Appearance.count() == 2
        appearance.roles == ["Neo", "Thomas Anderson"]
        appearance.from.name == "Keanu Reeves"
        appearance.to.title == "The Matrix"

    }
}

@Entity
class Person implements Node<Person> {
    String name
}

@Entity
class Appearance implements Relationship<Person, Movie> {
    List<String> roles = []
}

@Entity
class Movie implements Node<Movie> {
    String title
}
