package demos.part_02

import demos.MongoSpec
import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.mongodb.MongoEntity
import org.bson.conversions.Bson

import static com.mongodb.client.model.Filters.*

/**
 * - New methods added to the MongoEntity trait
 * - count(Bson)
 * - findOneAndDelete(Bson)
 */
class MoreNativeQueries extends MongoSpec {

    @Rollback
    def "There are new native methods within the MongoEntity trait for counting and finding!"() {
        when:
        new Club(name: "Real Madrid").save()
        new Club(name: "Barcelona").save()
        new Club(name: "Arsenal").save(flush:true)

        then:
        Bson filter = eq('name', "Barcelona")
        Club.findOneAndDelete(filter)
        Club.count(filter) == 0
        Club.count() == 2
    }
}

@Entity
class Club implements MongoEntity<Club> {
    String name
}