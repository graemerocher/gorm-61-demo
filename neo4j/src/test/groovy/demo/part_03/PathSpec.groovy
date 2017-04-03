package demo.part_03

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.neo4j.Node
import grails.neo4j.Path
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.grails.datastore.gorm.neo4j.Neo4jDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static grails.neo4j.mapping.MappingBuilder.node

/**
 * - New Methods have been added for Neo4j paths
 * - Calculate the Shortest path
 * - Or use Cypher to find a path
 */
class PathSpec extends Specification {

    @Shared @AutoCleanup Neo4jDatastore datastore = new Neo4jDatastore(getClass().getPackage())

    @Rollback
    def "You can define path queries to query the Graph"() {
        given:
        Person p = new Person(name: "Graeme")
        p.addToFriends(name: "Jeff")
            .friends.first()
                .addToFriends(name:"Alvaro" )
                .friends.first()
                    .addToFriends(name: "Ivan")
                        .friends.first()
                        .addToFriends(name: "Sergio")
        p.save(flush:true)

        when:
        Path<Person, Person> path = Person.findShortestPath(Person.load("Jeff"), Person.load("Sergio"))

        println(path.nodes().toList())

        for(Path.Segment<Person, Person> seg in path) {
            println "${seg.start()} is friends with ${seg.end()}"
        }

        then:
        path.contains(Person.load("Ivan"))

    }
}

@Entity
@ToString(includes = 'name')
@EqualsAndHashCode(includes = 'name')
class Person implements Node<Person> {
    String name
    static hasMany = [friends:Person]

    static mapping = node {
        id generator:'assigned', name:'name'
    }
}
