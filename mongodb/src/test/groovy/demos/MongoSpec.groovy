package demos

import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory
import org.grails.datastore.mapping.core.DatastoreUtils
import org.grails.datastore.mapping.mongo.MongoDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class MongoSpec extends Specification{

    @Shared MongodForTestsFactory factory = MongodForTestsFactory.with(Version.Main.PRODUCTION)
    @Shared @AutoCleanup MongoDatastore datastore = new MongoDatastore(
            factory.newMongo(),
            DatastoreUtils.createPropertyResolver(null),
            getClass().getPackage()
    )

    def cleanupSpec() {
        factory.shutdown()
    }

}
