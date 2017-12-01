package demos.part_07.services.transform

import grails.gorm.annotation.Entity
import grails.gorm.services.Query
import grails.gorm.services.Service
import grails.gorm.transactions.Rollback
import org.grails.orm.hibernate.HibernateDatastore
import org.hibernate.validator.constraints.NotBlank
import rx.Observable
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

/**
 * Great features of Data Services:
 *
 * - Compile time checking
 * - JPQ-QL queries
 * - javax.validation
 * - Automatic Transaction Handling
 * - Multi-Tenancy
 * - Abstract Classes
 * - No proxies
 *
 */
class DataServiceSpec extends Specification {
    @Shared @AutoCleanup HibernateDatastore datastore =
            new HibernateDatastore(getClass().getPackage())
    @Shared ClubService clubService = datastore.getService(ClubService)

    @Rollback
    void "You can now have your service logic automatically implemented!"() {
        when:
        clubService.saveClub("Real Madrid", 2, new Stadium(name: "Bernabéu", capacity: 80000))
        clubService.saveClub("Manchester United", 6000000, new Stadium(name: "Old Trafford", capacity: 78000))



        then:
        clubService.find("Manchester United").supporters == 6000000
        clubService.findClub("Manchester United").toBlocking().first().supporters == 6000000
        clubService.findStadiumCapacities() == [["Real Madrid", 80000], ["Manchester United", 78000]]

    }
}

@Entity
class Club {
    String name
    Integer supporters
    static hasOne = [stadium: Stadium]
}

@Entity
class Stadium {
    String name
    Integer capacity

    static belongsTo = [club:Club]
}

@Service(Club)
interface ClubService {

    Club saveClub(String name,
                  Integer supporters,
                  Stadium stadium)

    Club find( @NotBlank String name)

    Observable<Club> findClub(String name)

    @Query("""select $c.name, $s.capacity from ${Club c} 
                inner join ${Stadium s = c.stadium} """)
    List<Map> findStadiumCapacities()

    @Query("""update ${Club c} 
                set ${c.supporters} = $supporters 
                where $c.name = $name""")
    void updateSupporters(String name, Integer supporters)

}
