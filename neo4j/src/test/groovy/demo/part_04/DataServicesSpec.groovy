package demo.part_04

import grails.gorm.annotation.Entity
import grails.gorm.services.Service
import grails.gorm.transactions.Rollback
import grails.gorm.transactions.Transactional
import grails.neo4j.Node
import grails.neo4j.Relationship
import grails.neo4j.services.Cypher
import org.grails.datastore.gorm.neo4j.Neo4jDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

/**
 * - Additional integration with GORM Data Services for Neo4j
 * - Automatically implement Cypher queries!
 */
class DataServicesSpec extends Specification {
    @Shared
    @AutoCleanup
    Neo4jDatastore datastore = new Neo4jDatastore(getClass().getPackage())
    @Shared
    ClubService clubService = datastore.getService(ClubService)

    @Transactional
    def setupSpec() {
        new Club(name: "Real Madrid")
                .addToPlayers(name: "Ronaldo", age: 30)
                .addToPlayers(name: "Sergio Ramos", age: 30)
                .addToPlayers(name: "Pepe", age: 50)
                .save(flush: true)
    }


    @Rollback
    def "You can use the @Cypher annotation to automatically implement Cypher queries!"() {
        expect:
        clubService.findPlayers("Real Madrid").size() == 3
        clubService.findPlayers("Real Madrid").contains("Pepe")
    }

    @Rollback
    def "You can also find native relationships!"() {
        given:
        List<org.neo4j.driver.v1.types.Relationship> relationships = clubService.findClubPlayers("Real Madrid")
        for (rel in relationships) {
            println "Player ${Player.get(rel.endNodeId()).name} plays for ${Club.get(rel.startNodeId()).name}"
        }
        expect:
        relationships.size() == 3
    }

}

@Entity
class Club implements Node<Club> {
    String name
    static hasMany = [players: Player]
}

@Entity
class Player implements Node<Player> {
    String name
    int age
}

@Service(Club)
interface ClubService {

    @Cypher("""MATCH ${Club c}-[r:PLAYERS]->${Player p}
               WHERE $c.name = $name 
               RETURN $p.name""")
    List<String> findPlayers(String name)

    @Cypher("""MATCH ${Club c}-[r:PLAYERS]->${Player p} 
               WHERE $c.name = $name 
               RETURN r""")
    List<org.neo4j.driver.v1.types.Relationship> findClubPlayers(String name)

    @Cypher("""MATCH ${Player p}
               WHERE ${p.name} = ${player}
               SET ${p.age} =  ${age}""")
    void updateAge(String player, int age)
}