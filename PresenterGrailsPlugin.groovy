import es.osoco.grails.plugins.presenter.Presenter
import es.osoco.grails.plugins.presenter.ScopeAlwaysAsPrototypeResolver
import org.codehaus.groovy.grails.beans.factory.GenericBeanFactoryAccessor
import org.springframework.beans.BeanUtils
import org.springframework.context.ApplicationContext
import org.springframework.core.type.filter.AnnotationTypeFilter

class PresenterGrailsPlugin {
    def version = '0.1'
    def grailsVersion = '1.3.7 > *'
    def title = 'Presenter pattern implementation for Grails'
    def organization = [name: 'OSOCO', url: 'http://osoco.es/']
    def author = 'OSOCO'
    def authorEmail = 'info@osoco.es'
    def developers = [
        [name: 'Arturo García', email: 'arturo.garcia@osoco.es'],
        [name: 'Marcin Gryszko', email: 'marcin.gryszko@osoco.es'],
    ]

    def pluginExcludes = [
        'grails-app/domain/**/*',
        'grails-app/services/**/*',
        'src/groovy/es/osoco/grails/plugins/presenter/test/**/*'
    ]
    def description = '''Presenters decorate domain objects, adapting their interface to what a view requires.\n
They are able to expose domain object properties, transform them and generate HTML markup.'''

    def typeFilters = [new AnnotationTypeFilter(Presenter)]

    def doWithSpring = {
        xmlns grailsContext:"http://grails.org/schema/context"

        def packagesToScan = application.config.grails.presenters.packages
        if (packagesToScan) {
            grailsContext.'component-scan'(
                'base-package': packagesToScan.join(','),
                'scope-resolver': ScopeAlwaysAsPrototypeResolver.name
            )
        }
    }

    def doWithDynamicMethods = { ctx ->
        for (domainClass in application.domainClasses) {
            domainClass.metaClass.decorate = {
            }
        }

        def accessor = new GenericBeanFactoryAccessor(ctx)
        accessor.getBeansWithAnnotation(Presenter).each { name, presenter ->
            enhancePresenter presenter.class, name, ctx
        }
    }

    private static enhancePresenter(Class clazz, String name, ApplicationContext ctx) {
        def metaClass = GroovySystem.metaClassRegistry.getMetaClass(clazz)
        metaClass.constructor = { ->
            if (ctx.containsBean(name)) {
                ctx.getBean name
            }
            else {
                BeanUtils.instantiateClass clazz
            }
        }
    }
}
