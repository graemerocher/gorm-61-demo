package demos.part_06.validation

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import org.grails.orm.hibernate.HibernateDatastore
import org.hibernate.validator.constraints.NotBlank
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import javax.validation.constraints.Min

/**
 * - javax.validation annotations supported
 * - if you prefer them to constraints block you can use them!
 * - ConstraintException translated into Errors object
 */
class ValidationSpec extends Specification {

    @Shared @AutoCleanup HibernateDatastore datastore = new HibernateDatastore(getClass().getPackage())

    @Rollback
    def "If you prefer you can now use Bean Validation API constraints!"() {
        when:
        def insect = new Insect(name: "", numberOfLegs: 4)
        insect.save()

        then:
        insect.hasErrors()
        insect.errors.hasFieldErrors("name")
        insect.errors.hasFieldErrors("numberOfLegs")
        Insect.count() == 0
    }
}

@Entity
class Insect {
    @NotBlank
    String name
    @Min(6L)
    int numberOfLegs = 6
}
