package demos.part_07.services.common

import grails.gorm.multitenancy.TenantService
import grails.gorm.transactions.TransactionService
import org.grails.datastore.mapping.model.DatastoreConfigurationException
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

/**
 * - New common services
 * - TenantService
 * - TransactionService
 */
class CommonServicesSpec extends Specification{
    @Shared @AutoCleanup HibernateDatastore datastore = new HibernateDatastore(getClass().getPackage())
    @Shared TransactionService transactionService = datastore.getService(TransactionService)
    @Shared TenantService tenantService = datastore.getService(TenantService)


    def "There are new common services like TransactionService"() {
        expect:
        transactionService.withNewTransaction {
            "Great!"
        }
    }

    def "... and TenantService!"() {
        when:
        tenantService.withId("mytenant") {
            "Great!"
        }

        then:
        def e = thrown(DatastoreConfigurationException)
        e.message.endsWith("is not configured for Multi-Tenancy")
    }
}
