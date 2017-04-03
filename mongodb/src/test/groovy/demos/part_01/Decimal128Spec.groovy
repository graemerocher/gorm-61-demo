package demos.part_01

import demos.MongoSpec
import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback

/**
 * - MongoDB 3.4 adds a new Decimal128 type
 * - GORM 6.1 stores BigDecimal values using this type instead of toString()
 *
 */
class Decimal128Spec extends MongoSpec {

    @Rollback
    void "MongoDB now has support for a new Decimal128 type"() {
        when:
        new Boss(name: "Bob", salary: new BigDecimal(60000.00d)).save(flush:true)

        then:
        Boss.count() == 1
        Boss.first().salary == new BigDecimal(60000.00d)
    }

}

@Entity
class Boss {
    String name
    BigDecimal salary
}